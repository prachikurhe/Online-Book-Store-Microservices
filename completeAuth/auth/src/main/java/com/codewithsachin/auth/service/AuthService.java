package com.codewithsachin.auth.service;

import com.codewithsachin.auth.dto.*;
import com.codewithsachin.auth.entity.RefreshToken;
import com.codewithsachin.auth.entity.RoleEntity;
import com.codewithsachin.auth.entity.UserEntity;
import com.codewithsachin.auth.mapper.UserMapper;
import com.codewithsachin.auth.repository.RefreshTokenRepository;
import com.codewithsachin.auth.repository.RoleRepository;
import com.codewithsachin.auth.repository.UserRepository;
import com.codewithsachin.auth.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.codewithsachin.auth.constants.CommonConstants.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(USERNAME_ALREADY_EXISTS);
        }

        UserEntity user = userMapper.toUser(request, passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Delete existing refresh token
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush(); // ✅ Ensure deletion is applied before insert

        // Save new token
        saveRefreshToken(user, refreshToken);

        return new TokenResponse(accessToken, refreshToken, BEARER);
    }

    public void assignRole(RoleRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        RoleEntity role = roleRepository.findByName(request.getRole())
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().name(request.getRole()).build()));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException(INVALID_REFRESH_TOKEN));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException(REFRESH_TOKEN_EXPIRED);
        }

        UserEntity user = token.getUser();

        // 🔥 Important: Delete old token before creating a new one
        refreshTokenRepository.deleteByUser(user);
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, newRefreshToken); // rotate

        return new TokenResponse(newAccessToken, newRefreshToken, BEARER);
    }

    @Transactional
    public void logout(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        refreshTokenRepository.deleteByUser(user);
    }

    public UserResponse getCurrentUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    private void saveRefreshToken(UserEntity user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .expiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
