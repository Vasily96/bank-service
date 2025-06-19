package com.example.transfermoney.testcontainer;

import com.example.transfermoney.config.AbstractPostgresTest;
import com.example.transfermoney.model.dto.account.AccountDto;
import com.example.transfermoney.model.dto.user.UserDto;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.repository.UserRepository;
import com.example.transfermoney.service.UserService;
import com.example.transfermoney.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest extends AbstractPostgresTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private String validToken;

    private Long userId = 1L;

    private UserDto userDto;
    private User user;

    @BeforeAll
    void setup() {
        user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setPassword("{noop}password");
        userRepository.save(user);
    }

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setEmails(new ArrayList<>()); // инициализируем, чтобы не было null
        userDto.setPhones(new ArrayList<>());
        userDto.setAccount(new AccountDto(BigDecimal.valueOf(1100.00), BigDecimal.valueOf(1000.00)));
        validToken = jwtUtil.generateToken(1L);
    }

    @Test
    void shouldReturnValidResponseForExistingUser() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.dateOfBirth", notNullValue()))
                .andExpect(jsonPath("$.emails", hasSize(0)))
                .andExpect(jsonPath("$.phones", hasSize(0)))
                .andExpect(jsonPath("$.account.balance", is(1100.00)));
    }

}
