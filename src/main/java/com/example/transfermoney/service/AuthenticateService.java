package com.example.transfermoney.service;

import com.example.transfermoney.model.entity.EmailData;
import com.example.transfermoney.model.entity.PhoneData;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.repository.EmailDataRepository;
import com.example.transfermoney.repository.PhoneDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {

    private final PasswordEncoder passwordEncoder;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Transactional
    public User authenticate(String login, String password) {
        log.info("Authenticating user with login '{}'", login);
        User user = findUserByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        log.info("User '{}' authenticated successfully", login);
        return user;
    }

    private Optional<User> findUserByLogin(String login) {
        log.debug("Looking up user by login '{}'", login);
        return emailDataRepository.findByEmail(login).map(EmailData::getUser)
                .or(() -> phoneDataRepository.findByPhone(login)
                        .map(PhoneData::getUser));
    }
}
