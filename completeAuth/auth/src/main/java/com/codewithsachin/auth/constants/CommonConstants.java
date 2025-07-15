package com.codewithsachin.auth.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {
    public static final String BEARER = "Bearer";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
}
