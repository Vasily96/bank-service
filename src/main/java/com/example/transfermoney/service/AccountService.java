package com.example.transfermoney.service;

import com.example.transfermoney.model.entity.Account;
import com.example.transfermoney.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private static final Double MAX_BALANCE_MULTIPLIER = 2.07;
    public static final Double BALANCE_MULTIPLIER = 1.1;


    @Scheduled(fixedRate = 30000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void increaseBalance() {
        log.info("Starting scheduled balance increase for all accounts");
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            log.warn("No accounts found to update");
            return;
        }
        for (Account acc : accounts) {
            try {
                Account lockedAccount = accountRepository.findByIdWithLock(acc.getId())
                        .orElseThrow(() -> new RuntimeException("Account not found"));

                BigDecimal maxAllowed = lockedAccount.getInitialBalance().multiply(new BigDecimal(MAX_BALANCE_MULTIPLIER));
                BigDecimal newBalance = lockedAccount.getBalance().multiply(new BigDecimal(BALANCE_MULTIPLIER));

                log.debug("Processing account ID: {}", lockedAccount.getId());
                log.debug("Current balance: {}, New balance: {}, Max allowed: {}",
                        lockedAccount.getBalance(), newBalance, maxAllowed);

                if (newBalance.compareTo(maxAllowed) <= 0) {
                    lockedAccount.setBalance(newBalance);
                    accountRepository.save(lockedAccount);
                    log.info("Updated balance for account ID: {} to {}", lockedAccount.getId(), newBalance);
                } else {
                    log.warn("Skipping balance update for account ID: {} — max multiplier reached", lockedAccount.getId());
                }
            } catch (Exception e) {
                log.error("Error updating balance for account ID: {}", acc.getId(), e);
            }
        }
    }
}
