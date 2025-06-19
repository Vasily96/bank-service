package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.email.EmailCreateRequest;
import com.example.transfermoney.model.dto.email.EmailDto;
import com.example.transfermoney.model.entity.EmailData;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.model.mapper.EmailMapper;
import com.example.transfermoney.repository.EmailDataRepository;
import com.example.transfermoney.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.transfermoney.TestUtils.getTestEmailData;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private EmailDataRepository emailDataRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailMapper emailMapper;

    @InjectMocks
    private EmailService emailService;

    private EmailData emailData;
    private User user;


    @BeforeEach
    void setUp() {
        emailData = getTestEmailData();
        user = emailData.getUser();
    }

    @Test
    void addEmailSuccessfullyTest() {
        EmailCreateRequest request = new EmailCreateRequest();

        request.setEmail(emailData.getEmail());
        when(emailDataRepository.findByEmail(emailData.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(emailDataRepository.save(any(EmailData.class))).thenReturn(emailData);
        when(emailMapper.toDto(any(EmailData.class))).thenReturn(new EmailDto(emailData.getId(), emailData.getEmail()));
        emailService.addEmail(user.getId(), request);

        verify(emailDataRepository, times(1)).save(any(EmailData.class));
    }

    @Test
    void addEmailAlreadyExistsUnSuccessfullyTest() {
        EmailCreateRequest request = new EmailCreateRequest();
        request.setEmail("john@example.com");

        when(emailDataRepository.findByEmail("john@example.com")).thenReturn(Optional.of(emailData));

        assertThrows(RuntimeException.class, () -> emailService.addEmail(user.getId(), request));
        verify(emailDataRepository, never()).save(any(EmailData.class));
    }
}
