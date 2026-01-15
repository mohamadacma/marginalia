package com.marginalia.marginalia.dto;

import lombok.Data;

@Data
public class AddFriendRequest {
    private Long userId;
    private Long friendId;
}