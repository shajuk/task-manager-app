package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ErrorDetails;
import com.example.taskmanager.dto.TokenRequest;
import com.example.taskmanager.dto.TokenResponse;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/api/token")
    public ResponseEntity<TokenResponse> issueToken(@Valid @RequestBody TokenRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for user: {}", req.getUsername());
            TokenResponse tokenResponse=TokenResponse.builder()
                    .token(null)
                    .expiresIn(null)
                    .error(new ErrorDetails("HTTP-401", "Invalid Username or Password"))
                    .build();
            return ResponseEntity.status(401).body(tokenResponse);
        }

        User user = userService.findByUsername(req.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found: " + req.getUsername()));
        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
        TokenResponse tokenResponse = TokenResponse.builder()
                .token(token)
                .expiresIn(jwtUtil.getValidityMs())
                .build();
        return ResponseEntity.ok(tokenResponse);
    }
}
