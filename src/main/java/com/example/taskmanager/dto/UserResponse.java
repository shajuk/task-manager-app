package com.example.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Response body for user registration")
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    @Schema(description = "Success message", example = "User registered successfully")
    private String message;
    
    @Schema(description = "Registered username", example = "john_doe")
    private String userName;
    
    @Schema(description = "Error details if registration failed")
    private ErrorDetails error;
}
