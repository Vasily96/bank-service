package com.example.transfermoney.controller;

import com.example.transfermoney.model.dto.phone.PhoneCreateRequest;
import com.example.transfermoney.model.dto.phone.PhoneDto;
import com.example.transfermoney.model.dto.phone.PhoneUpdateRequest;
import com.example.transfermoney.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{id}/phones")
@RequiredArgsConstructor
@Tag(name = "Phone Management", description = "Operations for managing user phones")
@SecurityRequirement(name = "bearerAuth")
public class PhoneController {

    private final PhoneService phoneService;

    @PostMapping
    @Operation(summary = "Add a new phone to a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Phone successfully added"),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            })
    public ResponseEntity<PhoneDto> addPhone(@PathVariable Long id,
                                             @Valid @RequestBody PhoneCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(phoneService.addPhone(id, request));
    }

    @PatchMapping
    @Operation(summary = "Update an existing phone",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Phone successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Validation or business logic error"),
                    @ApiResponse(responseCode = "404", description = "Phone not found")
            })
    public ResponseEntity<PhoneDto> updatePhone(@PathVariable Long id,
                                                @Valid @RequestBody PhoneUpdateRequest request) {

        return ResponseEntity.ok(phoneService.updatePhone(id, request));
    }

    @DeleteMapping("/{phoneId}")
    @Operation(summary = "Delete a phone by its ID",
            parameters = {
                    @Parameter(name = "phone", description = "Phone number to delete", in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Phone successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Validation or business logic error"),
                    @ApiResponse(responseCode = "404", description = "Phone not found")
            })
    public ResponseEntity<Void> deletePhone(@PathVariable Long id,
                                            @PathVariable Long phoneId) {
        phoneService.deletePhone(id, phoneId);
        return ResponseEntity.noContent().build();
    }
}