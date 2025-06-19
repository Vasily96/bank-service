package com.example.transfermoney.model.dto.phone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneUpdateRequest {
    private Long id;
    @NotBlank(message = "Old phone is required")
    @Pattern(regexp = "^7\\d{10}$", message = "Old phone must be valid")
    @Schema(example = "71234567890", description = "Old Phone number")
    private String oldPhone;

    @NotBlank(message = "New phone is required")
    @Pattern(regexp = "^7\\d{10}$", message = "New phone must be valid")
    @Schema(example = "71234567899", description = "New Phone number")
    private String newPhone;
}