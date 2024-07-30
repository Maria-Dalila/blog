package com.dalila.blog.dto;

import com.dalila.blog.entities.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponse {
    private UUID id;
    private String content;
    private String author;
    private LocalDateTime creationDate;

    public static CommentResponse convertToResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreationDate()
        );
    }

}
