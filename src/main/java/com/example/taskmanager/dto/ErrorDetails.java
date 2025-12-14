package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Details about the error")
@Data
@AllArgsConstructor
public class ErrorDetails {
    @Schema(description = "Error code", example = "HTTP-401")
    private String errorCode;
    
    @Schema(description = "Error message", example = "Invalid Username or Password")
    private String errorMessage;
}