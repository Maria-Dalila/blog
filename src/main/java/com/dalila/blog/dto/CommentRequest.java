package com.dalila.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequest {

    @JsonInclude
    private UUID commentId;
    @JsonInclude
    private UUID postId;
    @JsonInclude
    private String content;

}
