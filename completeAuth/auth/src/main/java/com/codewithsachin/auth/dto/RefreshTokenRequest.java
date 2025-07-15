package com.codewithsachin.auth.dto;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}