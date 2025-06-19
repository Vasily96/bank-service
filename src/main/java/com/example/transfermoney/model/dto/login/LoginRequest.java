package com.example.transfermoney.model.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Login is required")
    @Schema(example = "topuser", description = "Username for login")
    private String login;

    @NotBlank(message = "Password is required")
    @Schema(example = "password", description = "Password for login")
    private String password;
}
