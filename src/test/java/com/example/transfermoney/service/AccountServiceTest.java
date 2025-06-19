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
import java.util.List;

import static com.example.transfermoney.TestUtils.getTestAccount;
import static com.example.transfermoney.service.AccountService.BALANCE_MULTIPLIER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = getTestAccount();
    }

    @Test
    void increaseBalanceSuccessfullyTest() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(account));

        accountService.increaseBalance();

        assertEquals(account.getInitialBalance().multiply(new BigDecimal(BALANCE_MULTIPLIER)), account.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void increaseBalanceAlreadyMaxUnSuccessfullyTest() {
        account.setBalance(BigDecimal.valueOf(207.00));
        account.setInitialBalance(BigDecimal.valueOf(100.00));

        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(account));

        accountService.increaseBalance();

        assertEquals(BigDecimal.valueOf(207.00), account.getBalance());
        verify(accountRepository, never()).save(any(Account.class));
    }
}