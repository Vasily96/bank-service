package com.example.transfermoney.controller;


import com.example.transfermoney.model.dto.login.LoginRequest;
import com.example.transfermoney.model.dto.login.LoginResponse;
import com.example.transfermoney.service.AuthenticateService;
import com.example.transfermoney.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Operations for user authentication")
@RequiredArgsConstructor
public class AuthController {


    private final JwtUtil jwtUtil;
    private final AuthenticateService service;


    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var auth =
                service.authenticate(request.getLogin(), request.getPassword());
        String token = jwtUtil.generateToken(auth.getId());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
