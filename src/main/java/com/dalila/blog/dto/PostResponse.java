package com.dalila.blog.dto;

import com.dalila.blog.entities.Comment;
import com.dalila.blog.entities.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private UUID id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private String author;
    private List<Comment> comments;

    public static PostResponse convertToResponse(Post post){

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreationDate(),
                post.getAuthor(),
                post.getComments()
        );

    }
}
