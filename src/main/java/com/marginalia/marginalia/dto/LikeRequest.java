package com.marginalia.marginalia.dto;

import lombok.Data;

@Data
public class LikeRequest {
    private Long userId;
    private Long annotationId;
}