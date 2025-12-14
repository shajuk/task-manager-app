package com.example.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Response body for token generation")
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {

    @Schema(description = "Generated JWT token", example = "eyJhbGciOiJIUzI1...")
    private String token;
    @Schema(description = "Token expiration time in seconds", example = "3600")
    private Long expiresIn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorDetails error;

    public TokenResponse(String token, Long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
