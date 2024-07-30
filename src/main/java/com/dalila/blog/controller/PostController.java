package com.dalila.blog.controller;

import com.dalila.blog.dto.CommentRequest;
import com.dalila.blog.dto.CommentResponse;
import com.dalila.blog.dto.PostRequest;
import com.dalila.blog.dto.PostResponse;
import com.dalila.blog.service.IPostService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {

//colocar o awtowired e retirar o final e o construtor - solid inversão  de dependencia,
// na interface n anota nada, na implementação @component
    //pesquisar por primary e qualifier anotations para se vc tiver duas implementações
    private final IPostService postService;

    @Autowired
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        if(postRequest.getTitle().equals("") || postRequest.getContent().equals("")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(postService.createPost(postRequest));
    }

    @PutMapping()
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostRequest post){
        PostResponse postResponse = postService.updatePost(post);
        if(postResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping()
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.ok(postService.findAllPosts());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable UUID postId){
        boolean status = postService.deletePost(postId);
        if(!status){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado ou usuário sem permissão!");
        }
        return ResponseEntity.ok("Artigo deletado com sucesso!");
    }

    @GetMapping("/find/{keyword}")
    public ResponseEntity<List<PostResponse>> findByKeyword(@PathVariable String keyword){
        return ResponseEntity.ok(postService.findByKeyword(keyword));
    }


    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> newComment(@RequestBody CommentRequest commentRequest){
        try {
            if (commentRequest.getPostId() != null && !commentRequest.getContent().equals("")) {
                var result = postService.newComment(commentRequest);
                if (result == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                return ResponseEntity.ok(result);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/comment")
    public ResponseEntity<String> deleteComment(@RequestBody CommentRequest commentRequest){
        if(commentRequest.getPostId() != null && commentRequest.getCommentId() != null ){
            boolean response = postService.deleteComment(commentRequest);
            if (!response){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir comentário");
            }
            else{
                return ResponseEntity.ok("Comentário excluído com sucesso!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir comentário");
    }

    
}
