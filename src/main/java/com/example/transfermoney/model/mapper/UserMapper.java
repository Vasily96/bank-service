package com.example.transfermoney.model.mapper;

import com.example.transfermoney.model.dto.account.AccountDto;
import com.example.transfermoney.model.dto.email.EmailDto;
import com.example.transfermoney.model.dto.phone.PhoneDto;
import com.example.transfermoney.model.dto.user.UserDto;
import com.example.transfermoney.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getDateOfBirth(),
                Optional.ofNullable(user.getEmails()).orElse(Collections.emptySet())
                        .stream()
                        .map(e -> new EmailDto(user.getId(), e.getEmail()))
                        .toList(),
                Optional.ofNullable(user.getPhones()).orElse(Collections.emptySet())
                        .stream()
                        .map(p -> new PhoneDto(p.getId(), p.getPhone()))
                        .toList(),
                user.getAccount() != null ?
                        new AccountDto(user.getAccount().getBalance(), user.getAccount().getInitialBalance()) : null
        );
    }
}
