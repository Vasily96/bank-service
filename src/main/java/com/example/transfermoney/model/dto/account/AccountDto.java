package com.example.transfermoney.model.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @Schema(example = "1000.50", description = "Current user's balance")
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be at least 0.00")
    @Digits(integer = 19, fraction = 2, message = "Balance must have up to 19 digits and 2 decimal places")
    private BigDecimal balance;

    @Schema(example = "1000.50", description = "Initial user's balance")
    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Initial balance must be at least 0.00")
    @Digits(integer = 19, fraction = 2, message = "Initial balance must have up to 19 digits and 2 decimal places")
    private BigDecimal initialBalance;
}
