package com.example.transfermoney.service;

import com.example.transfermoney.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testGenerateAndExtractUserId() {
        String token = jwtUtil.generateToken(1L);
        Long userId = jwtUtil.extractUserId(token);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void testInvalidTokenReturnsFalse() {
        String invalidToken = "invalid.token.value";
        boolean isValid = jwtUtil.isTokenValid(invalidToken);
        assertThat(isValid).isFalse();
    }
}
