package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.user.UserDto;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.model.mapper.UserMapper;
import com.example.transfermoney.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.transfermoney.TestUtils.getEmptyUser;
import static com.example.transfermoney.TestUtils.getTestUserDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private Long userId = 3L;

    @BeforeEach
    void setUp() {
        user = getEmptyUser();

    }

    @Test
    void getUserByIdSuccessfullyTest() {
        UserDto userDto = getTestUserDTO();
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(mapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdNotFoundUnSuccessfullyTest() {
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}