package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.phone.PhoneCreateRequest;
import com.example.transfermoney.model.dto.phone.PhoneDto;
import com.example.transfermoney.model.entity.PhoneData;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.model.mapper.PhoneMapper;
import com.example.transfermoney.repository.PhoneDataRepository;
import com.example.transfermoney.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.transfermoney.TestUtils.getTestPhoneData;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneServiceTest {

    @Mock
    private PhoneDataRepository phoneDataRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneMapper phoneMapper;

    @InjectMocks
    private PhoneService phoneService;

    private PhoneData phoneData;

    private User user;

    @BeforeEach
    void setUp() {
        phoneData = getTestPhoneData();
        user = phoneData.getUser();
    }

    @Test
    void addPhoneSuccessfullyTest() {
        PhoneCreateRequest request = new PhoneCreateRequest();
        request.setPhone("79201234568");

        when(phoneDataRepository.findByPhone("79201234568")).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(phoneDataRepository.save(any(PhoneData.class))).thenReturn(phoneData);
        when(phoneMapper.toDto(any(PhoneData.class))).thenReturn(new PhoneDto(phoneData.getId(), phoneData.getPhone()));
        phoneService.addPhone(user.getId(), request);

        verify(phoneDataRepository, times(1)).save(any(PhoneData.class));
    }

    @Test
    void addPhoneAlreadyExistsUnSuccessfullyTest() {
        PhoneCreateRequest request = new PhoneCreateRequest();
        request.setPhone("79201234567");

        when(phoneDataRepository.findByPhone("79201234567")).thenReturn(Optional.of(phoneData));

        assertThrows(RuntimeException.class, () -> phoneService.addPhone(user.getId(), request));
        verify(phoneDataRepository, never()).save(any(PhoneData.class));
    }
}
