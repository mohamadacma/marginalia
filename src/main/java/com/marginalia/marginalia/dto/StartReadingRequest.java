package com.marginalia.marginalia.dto;

import lombok.Data;

@Data
public class StartReadingRequest {
    private Long userId;
    private Long bookId;
}