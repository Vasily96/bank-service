package com.example.transfermoney.controller;

import com.example.transfermoney.model.dto.transfer.TransferRequest;
import com.example.transfermoney.model.dto.transfer.TransferResponse;
import com.example.transfermoney.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/transfers",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Transfers", description = "Money transfer operations")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferService transferService;

    @PatchMapping
    @Operation(summary = "Transfer money between users", description = "Requires valid JWT token")
    @ApiResponse(responseCode = "200", description = "Transfer successful")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public ResponseEntity<TransferResponse> transfer(
            @Parameter(description = "Transfer data (fromUserId, toUserId, amount)", required = true)
            @Valid @RequestBody TransferRequest request) {

        return ResponseEntity.ok(
                transferService.transferMoney(request.getFromUserId(), request.getToUserId(), request.getAmount()));
    }
}
