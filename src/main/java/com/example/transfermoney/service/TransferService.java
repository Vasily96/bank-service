package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.transfer.TransferResponse;
import com.example.transfermoney.model.entity.Account;
import com.example.transfermoney.repository.AccountRepository;
import com.example.transfermoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransferResponse transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        log.info("Starting money transfer: fromUserId={}, toUserId={}, amount={}", fromUserId, toUserId, amount);
        Account fromAcc = getAccountWithLock(fromUserId);
        Account toAcc = getAccountWithLock(toUserId);
        log.debug("Retrieved accounts - From: {} (balance: {}), To: {} (balance: {})",
                fromAcc.getId(), fromAcc.getBalance(), toAcc.getId(), toAcc.getBalance());

        validateTransfer(fromAcc, toAcc, amount);

        BigDecimal newFromBalance = fromAcc.getBalance().subtract(amount);
        BigDecimal newToBalance = toAcc.getBalance().add(amount);

        fromAcc.setBalance(newFromBalance);
        toAcc.setBalance(newToBalance);

        accountRepository.saveAll(List.of(fromAcc, toAcc));
        log.info("Transfer successful: {} transferred from {} to {}. New balances - From: {}, To: {}",
                amount, fromUserId, toUserId, newFromBalance, newToBalance);
        return new TransferResponse("SUCCESS", amount, newFromBalance, newToBalance);
    }

    private Account getAccountWithLock(Long userId) {
        return accountRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new RuntimeException("Account not found for user ID: " + userId));
    }

    private void validateTransfer(Account from, Account to, BigDecimal amount) {
        if (from.getUser().getId().equals(to.getUser().getId())) {
            throw new IllegalArgumentException("Cannot transfer to yourself");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough balance");
        }
        log.debug("Transfer validation passed for fromAccount={}, toAccount={}, amount={}",
                from.getId(), to.getId(), amount);
    }
}
