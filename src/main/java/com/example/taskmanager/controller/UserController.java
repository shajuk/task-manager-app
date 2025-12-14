package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ErrorDetails;
import com.example.taskmanager.dto.UserRequest;
import com.example.taskmanager.dto.UserResponse;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;

@Tag(name = "User Management", description = "Endpoints for user registration")
@Slf4j
@RestController
@RequiredArgsConstructor

public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "422", description = "User already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    })
    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest req) {

        if (userService.findByUsername(req.getUsername()).isPresent()) {
            UserResponse userResponse=UserResponse.builder()
                    .error(new ErrorDetails("HTTP-422", "Unable to register user please retry!"))
                    .build();
            log.warn("Unable to register user: {}", req.getUsername());
            return ResponseEntity.status(422).body(userResponse);
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .email(req.getEmail())
                .roles(new HashSet<String>() {{ add("ROLE_USER"); }})
                .build();
        userService.save(user);

        UserResponse userResponse=UserResponse.builder()
                .message("User registered successfully")
                .userName(user.getUsername())
                .build();
        return ResponseEntity.status(201).body(userResponse);
    }
}
