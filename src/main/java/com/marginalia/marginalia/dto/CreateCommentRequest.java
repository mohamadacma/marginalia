package com.marginalia.marginalia.dto;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String text;
    private Long userId;
    private Long annotationId;
}