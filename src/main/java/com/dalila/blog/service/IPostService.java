package com.dalila.blog.service;

import com.dalila.blog.dto.CommentRequest;
import com.dalila.blog.dto.CommentResponse;
import com.dalila.blog.dto.PostRequest;
import com.dalila.blog.dto.PostResponse;
import com.dalila.blog.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface IPostService {

     PostResponse createPost(PostRequest postRequest);
     PostResponse updatePost(PostRequest postRequest);
     List<PostResponse> findByKeyword(String keyword);
     List<PostResponse> findAllPosts();
     boolean deletePost(UUID id);
     CommentResponse newComment (CommentRequest commentRequest);
     boolean deleteComment(CommentRequest commentRequest);





}
