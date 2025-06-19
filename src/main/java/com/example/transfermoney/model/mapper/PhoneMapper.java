package com.example.transfermoney.model.mapper;

import com.example.transfermoney.model.dto.phone.PhoneDto;
import com.example.transfermoney.model.entity.PhoneData;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {

    public PhoneDto toDto(PhoneData data) {
        return new PhoneDto(data.getId(), data.getPhone());
    }
}
