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
public class PhoneDto {
    private Long id;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^7\\d{10}$", message = "Phone must start with 7 and have 11 digits")
    @Schema(example = "71234567890", description = "Phone number")
    private String phone;
}