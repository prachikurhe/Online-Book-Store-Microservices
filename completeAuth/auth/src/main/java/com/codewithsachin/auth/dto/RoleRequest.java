package com.codewithsachin.auth.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String username;
    private String role; // USER, ADMIN
}
