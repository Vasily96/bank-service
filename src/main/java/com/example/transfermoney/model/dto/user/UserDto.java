package com.example.transfermoney.model.dto.user;

import com.example.transfermoney.model.dto.account.AccountDto;
import com.example.transfermoney.model.dto.email.EmailDto;
import com.example.transfermoney.model.dto.phone.PhoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = "User ID is required")
    @Schema(example = "2", description = "User ID")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 500, message = "Name cannot exceed 500 characters")
    @Schema(example = "John Doe", description = "User name")
    private String name;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth must be in the past or today")
    @Schema(example = "1990-05-15", description = "User dateOfBirth")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "At least one email is required")
    @Valid
    @Schema(example = "test@mail.com", description = "User email list")
    private List<@Valid EmailDto> emails;

    @NotEmpty(message = "At least one phone is required")
    @Schema(example = "71234567890", description = "User phone list")
    @Valid
    private List<@Valid PhoneDto> phones;

    @NotNull(message = "Account information is required")
    @Valid
    @Schema(example = """
            {
            "balance" : "1000",
            "initailBalance" : "900"
            }
            """
            , description = "User account")
    private AccountDto account;
}
