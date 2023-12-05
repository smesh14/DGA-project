package com.contactbook.service;


import com.contactbook.config.JwtService;
import com.contactbook.entity.AuthRequest;
import com.contactbook.entity.AuthResponse;
import com.contactbook.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public record AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                          UserRepository userRepository) {

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.
                authenticate(new
                        UsernamePasswordAuthenticationToken
                        (request.getUserName(), request.getPassword()));
        var user = userRepository.findByName(request.getUserName()).orElseThrow();
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
