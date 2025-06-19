package com.example.transfermoney.service;

import com.example.transfermoney.model.entity.Account;
import com.example.transfermoney.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static com.example.transfermoney.TestUtils.getTestFromAccount;
import static com.example.transfermoney.TestUtils.getTestToAccount;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    private Account fromAccount;
    private Account toAccount;


    @BeforeEach
    void setUp() {
        fromAccount = getTestFromAccount();
        toAccount = getTestToAccount();
    }

    @Test
    void transferMoneySuccessfullyTest() {
        // Given
        when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(2L)).thenReturn(Optional.of(toAccount));

        // When
        transferService.transferMoney(1L, 2L, BigDecimal.valueOf(100.00));

        // Then
        verify(accountRepository, times(1)).findByIdWithLock(1L);
        verify(accountRepository, times(1)).findByIdWithLock(2L);
        verify(accountRepository, times(1)).saveAll(Arrays.asList(fromAccount, toAccount));
    }

    @Test
    void transferMoneyNotEnoughBalanceUnSuccessfullyTest() {
        fromAccount.setBalance(BigDecimal.ZERO);
        when(accountRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(2L)).thenReturn(java.util.Optional.of(toAccount));

        assertThrows(IllegalArgumentException.class, () ->
                transferService.transferMoney(1L, 2L, BigDecimal.valueOf(100.00)));

        verify(accountRepository, never()).save(any(Account.class));
    }
}
