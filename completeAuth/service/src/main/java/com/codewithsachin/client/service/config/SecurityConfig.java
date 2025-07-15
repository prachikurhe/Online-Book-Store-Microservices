package com.codewithsachin.client.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable()) // ✅ DISABLE CSRF GLOBALLY
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/user").hasRole("USER")
                        .pathMatchers("/admin").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(
                        resourceServer -> resourceServer.jwt(
                                Customizer.withDefaults()
                        )
                );
        return http.build();
    }
}
