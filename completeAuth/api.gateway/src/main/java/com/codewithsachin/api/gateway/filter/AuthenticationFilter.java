package com.codewithsachin.api.gateway.filter;

import com.codewithsachin.api.gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.codewithsachin.api.gateway.constants.CommonConstants.*;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilterFactory<AuthenticationFilter.Config> {


    private final JwtUtil jwtUtil;

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith(BEARER)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(BEARER.length()); // Strip "Bearer "

            if (!jwtUtil.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String username = jwtUtil.extractUsername(token);
            var roles = jwtUtil.extractRoles(token);

            exchange = exchange.mutate().request(request -> request
                    .header(X_USER_NAME, username)
                    .header(X_USER_ROLES, String.join(DELIMITER, roles))
            ).build();

            return chain.filter(exchange);
        };
    }
}


