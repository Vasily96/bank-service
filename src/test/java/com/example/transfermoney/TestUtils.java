package com.example.transfermoney;

import com.example.transfermoney.model.dto.user.UserDto;
import com.example.transfermoney.model.entity.Account;
import com.example.transfermoney.model.entity.EmailData;
import com.example.transfermoney.model.entity.PhoneData;
import com.example.transfermoney.model.entity.User;

import java.math.BigDecimal;

public class TestUtils {

    public static Account getTestAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));
        account.setInitialBalance(BigDecimal.valueOf(100.00));
        return account;
    }

    public static User getEmptyUser() {
        return User.builder().name("John Doe").id(3L)
                .build();
    }

    public static EmailData getTestEmailData() {
        EmailData emailData = new EmailData();
        emailData.setId(1L);
        emailData.setEmail("john@example.com");
        emailData.setUser(getEmptyUser());
        return emailData;
    }

    public static PhoneData getTestPhoneData() {
        PhoneData phoneData = new PhoneData();
        phoneData.setId(1L);
        phoneData.setPhone("79201234567");
        phoneData.setUser(getEmptyUser());
        return phoneData;
    }

    public static Account getTestFromAccount() {
        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(1000.00));
        fromAccount.setInitialBalance(BigDecimal.valueOf(1000.00));
        fromAccount.setUser(User.builder().name("John").id(1L).account(fromAccount).build());
        return fromAccount;
    }

    public static Account getTestToAccount() {
        Account fromAccount = new Account();
        fromAccount.setId(2L);
        fromAccount.setBalance(BigDecimal.valueOf(700.00));
        fromAccount.setInitialBalance(BigDecimal.valueOf(700.00));
        fromAccount.setUser(User.builder().name("Fedor").id(2L).account(fromAccount).build());
        return fromAccount;
    }

    public static UserDto getTestUserDTO() {
        User user = getEmptyUser();
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        return userDto;
    }
}
