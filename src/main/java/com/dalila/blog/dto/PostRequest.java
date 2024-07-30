package com.dalila.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostRequest {

    @JsonInclude
    private UUID id;
    private String title;
    private String content;

}
