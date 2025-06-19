package com.example.transfermoney.controller;

import com.example.transfermoney.model.dto.email.EmailCreateRequest;
import com.example.transfermoney.model.dto.email.EmailDto;
import com.example.transfermoney.model.dto.email.EmailUpdateRequest;
import com.example.transfermoney.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{id}/emails")
@RequiredArgsConstructor
@Tag(name = "Email Management", description = "Operations for managing user emails")
@SecurityRequirement(name = "bearerAuth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    @Operation(summary = "Add a new email to a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Email successfully added"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    public ResponseEntity<EmailDto> addEmail(@PathVariable Long id,
                                             @Valid @RequestBody EmailCreateRequest request) {
        ;
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.addEmail(id, request));
    }

    @PatchMapping
    @Operation(summary = "Update an existing email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Validation or business logic error"),
                    @ApiResponse(responseCode = "404", description = "Email not found")
            })
    public ResponseEntity<EmailDto> updateEmail(@PathVariable Long id,
                                                @Valid @RequestBody EmailUpdateRequest request) {

        return ResponseEntity.ok(emailService.updateEmail(id, request));
    }

    @DeleteMapping("/{emailId}")
    @Operation(summary = "Delete an email by its ID")
    @ApiResponse(responseCode = "200", description = "Email successfully deleted")
    @ApiResponse(responseCode = "404", description = "Email or User not found")
    public ResponseEntity<Void> deleteEmail(
            @PathVariable("id") Long userId,
            @PathVariable("emailId") Long emailId) {
        emailService.deleteEmail(userId, emailId);
        return ResponseEntity.noContent().build();
    }
}
