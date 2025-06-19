package com.example.transfermoney.model.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse implements Serializable {
    private String status;
    private BigDecimal amount;
    private BigDecimal newBalanceFrom;
    private BigDecimal newBalanceTo;
}