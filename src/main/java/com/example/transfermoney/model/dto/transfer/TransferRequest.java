package com.example.transfermoney.model.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "From user ID is required")
    @Schema(example = "1", description = "From user ID")
    private Long fromUserId;

    @NotNull(message = "To user ID is required")
    @Schema(example = "2", description = "To user ID")
    private Long toUserId;

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Schema(example = "200", description = "Transfer's amount")
    private BigDecimal amount;
}
