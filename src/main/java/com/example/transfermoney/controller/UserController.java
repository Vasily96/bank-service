package com.example.transfermoney.controller;

import com.example.transfermoney.model.dto.user.UserDto;
import com.example.transfermoney.model.dto.user.UserSearchCriteria;
import com.example.transfermoney.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID",
            parameters = {
                    @Parameter(name = "id", description = "User ID", in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found user"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by criteria",
            parameters = {
                    @Parameter(name = "name", description = "Filter by name (LIKE 'name%')", in = ParameterIn.QUERY),
                    @Parameter(name = "email", description = "Filter by exact email match", in = ParameterIn.QUERY),
                    @Parameter(name = "phone", description = "Filter by exact phone match", in = ParameterIn.QUERY),
                    @Parameter(name = "dateOfBirth", description = "Filter users born after this date", example = "1990-01-01", in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Number of results per page", in = ParameterIn.QUERY),
                    @Parameter(name = "page", description = "Page number", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page of users"),
                    @ApiResponse(responseCode = "400", description = "Invalid search criteria")
            })
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "dateOfBirth", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @PageableDefault(size = 10) Pageable pageable) {

        UserSearchCriteria criteria = new UserSearchCriteria(name, email, phone, dateOfBirth);
        return ResponseEntity.ok(userService.searchUsers(criteria, pageable));
    }
}
