package com.marginalia.marginalia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnnotationWithLikesDTO {
    private Long annotationId;
    private String text;
    private Integer pageNumber;
    private String username;  // Who wrote it
    private String bookTitle;
    private Long likeCount;
}