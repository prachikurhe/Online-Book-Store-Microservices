package com.codewithsachin.auth.service;


import com.codewithsachin.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user ->
                        User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .authorities(user.getRoles().stream()
                                        .map(role -> "ROLE_" + role.getName())
                                        .toArray(String[]::new))
                                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}