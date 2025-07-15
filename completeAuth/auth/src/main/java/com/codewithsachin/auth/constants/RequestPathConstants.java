package com.codewithsachin.auth.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPathConstants {
    public static final String AUTH_API = "/auth/**";
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String ASSIGN_ROLE = "/assign-role";
    public static final String AUTH_REFRESH = "/auth/refresh";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String H_2_CONSOLE = "/h2-console/**";
    public static final String AUTH_ME = "/auth/me";
    public static final String ACTUATOR_HEALTH = "/actuator/health";
    public static final String ACTUATOR_INFO = "/actuator/info";
}
