package com.dalila.blog.service;

import com.dalila.blog.dto.CommentRequest;
import com.dalila.blog.dto.CommentResponse;
import com.dalila.blog.dto.PostRequest;
import com.dalila.blog.dto.PostResponse;
import com.dalila.blog.entities.Comment;
import com.dalila.blog.entities.Post;
import com.dalila.blog.entities.Role;
import com.dalila.blog.entities.User;
import com.dalila.blog.exception.PostNotFoundException;
import com.dalila.blog.repository.CommentRepository;
import com.dalila.blog.repository.PostRepository;
import com.dalila.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements IPostService {


    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    private User getRequestingUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Override
    public PostResponse createPost(PostRequest postRequest) {
        String username = getRequestingUser().getUsername();
        List<Comment> comments = new ArrayList<>();

        Post post = new Post(
                null,
                postRequest.getTitle(),
                postRequest.getContent(),
                LocalDateTime.now(),
                username,
                comments
        );

        Post data = postRepository.save(post);
        System.out.println(data.getComments());

        return PostResponse.builder()
                .id(data.getId())
                .title(data.getTitle())
                .content(data.getContent())
                .creationDate(data.getCreationDate())
                .author(data.getAuthor())
                .comments(data.getComments())
                .build();

    }

    @Override
    public PostResponse updatePost(PostRequest postRequest) {
        Optional<Post> postOptional = postRepository.findById(postRequest.getId());
        if(postOptional.isPresent()){
          Post post = postOptional.get();
          post.setTitle(postRequest.getTitle());
          post.setContent(postRequest.getContent());

          return PostResponse.convertToResponse(postRepository.save(post));
        }

        return null;

    }

    @Override
    public List<PostResponse> findByKeyword(String keyword) {
        List<Post> posts = postRepository.findByKeyword(keyword);
        return posts.stream().map(PostResponse::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostResponse::convertToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deletePost(UUID id) {
        User user =  getRequestingUser();
        if (user.getRole().equals(Role.USER)){
            return false;
        }

        if(!postRepository.existsById(id)){
            return false;
        }

        postRepository.deleteById(id);
        return true;

    }

    @Override
    public CommentResponse newComment (CommentRequest commentRequest){
       Optional<Post> optional = postRepository.findById(commentRequest.getPostId());

       if(optional.isPresent()){
           User user = getRequestingUser();
           Post post = optional.get();
           Comment comment = new Comment(
                   null,
                   user.getUsername(),
                   commentRequest.getContent(),
                   LocalDateTime.now()
           );
           Comment response = commentRepository.save(comment);
           post.addComment(response);
           postRepository.save(post);

          return CommentResponse.convertToResponse(response);
       }
       else{
           return null;
       }
    }

    @Override
    public boolean deleteComment(CommentRequest commentRequest) {
        User user = getRequestingUser();
        Optional<Comment> commentOptional = commentRepository.findById(commentRequest.getCommentId());
        Optional<Post> postOptional = postRepository.findById(commentRequest.getPostId());

        if(commentOptional.isPresent() && postOptional.isPresent()) {
            Comment comment = commentOptional.get();
            Post post = postOptional.get();

            if(user.getEmail().equals(comment.getAuthor()) || user.getRole().equals(Role.ADMIN)) {
                post.removeComment(comment);
                postRepository.save(post);
                commentRepository.deleteById(commentRequest.getCommentId());
                return true;
            }
        }
        return false;
    }
}
