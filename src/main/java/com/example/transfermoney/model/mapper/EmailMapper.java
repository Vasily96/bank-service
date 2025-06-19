package com.example.transfermoney.model.mapper;

import com.example.transfermoney.model.dto.email.EmailDto;
import com.example.transfermoney.model.entity.EmailData;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public EmailDto toDto(EmailData emailData) {
        return new EmailDto(
                emailData.getId(),
                emailData.getEmail()
        );
    }
}
