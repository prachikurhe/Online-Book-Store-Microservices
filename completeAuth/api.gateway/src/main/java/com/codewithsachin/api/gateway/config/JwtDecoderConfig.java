package com.codewithsachin.api.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtDecoderConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] secret = secretKey.getBytes();
        return NimbusReactiveJwtDecoder.withSecretKey(
                new javax.crypto.spec.SecretKeySpec(secret, "HmacSHA256")
        ).build();
    }
}

