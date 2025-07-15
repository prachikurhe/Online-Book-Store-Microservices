package com.codewithsachin.auth.filter;

import com.codewithsachin.auth.entity.UserEntity;
import com.codewithsachin.auth.repository.RefreshTokenRepository;
import com.codewithsachin.auth.repository.UserRepository;
import com.codewithsachin.auth.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7); // remove "Bearer "

        try {
            username = jwtService.extractUsername(jwtToken);
        } catch (ExpiredJwtException e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ Ensure token is valid
            if (jwtService.isTokenValid(jwtToken)) {

                // 🔐 Additional check: is the refresh token still present in DB?
                UserEntity user = userRepository.findByUsername(username)
                        .orElse(null);

                if (user == null || !refreshTokenRepository.existsByUser(user)) {
                    // User has logged out — refresh token deleted
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Access token invalidated due to logout");
                    return;
                }

                // ✅ Proceed with authentication
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
