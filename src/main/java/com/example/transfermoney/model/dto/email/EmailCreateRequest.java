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
public class EmailCreateRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(example = "test@mail.com", description = "Email")
    private String email;
}
