package com.example.transfermoney.model.dto.email;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailUpdateRequest {

    @NotBlank(message = "Old email is required")
    @Email(message = "Old email should be valid")
    @Schema(example = "test@mail.com", description = "old email")
    private String oldEmail;

    @NotBlank(message = "New email is required")
    @Email(message = "New email should be valid")
    @Schema(example = "newtest@mail.com", description = "new email")
    private String newEmail;
}
