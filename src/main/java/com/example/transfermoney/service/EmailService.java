package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.email.EmailCreateRequest;
import com.example.transfermoney.model.dto.email.EmailDto;
import com.example.transfermoney.model.dto.email.EmailUpdateRequest;
import com.example.transfermoney.model.entity.EmailData;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.model.mapper.EmailMapper;
import com.example.transfermoney.repository.EmailDataRepository;
import com.example.transfermoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;
    private final EmailMapper emailMapper;

    public List<EmailDto> getEmailsByUserId(Long userId) {
        log.debug("Fetching emails for user ID: {}", userId);
        return emailDataRepository.findAllByUserId(userId).stream()
                .map(emailMapper::toDto)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public EmailDto addEmail(Long userId, EmailCreateRequest request) {
        log.info("Adding new email '{}' for user ID: {}", request.getEmail(), userId);
        if (emailDataRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmailData emailData = new EmailData();
        emailData.setUser(user);
        emailData.setEmail(request.getEmail());
        log.info("Successfully added email '{}' for user ID: {}", request.getEmail(), userId);
        return emailMapper.toDto(emailDataRepository.save(emailData));
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public EmailDto updateEmail(Long userId, EmailUpdateRequest request) {
        log.info("Updating email from '{}' to '{}' for user ID: {}", request.getOldEmail(), request.getNewEmail(), userId);

        EmailData existing = emailDataRepository.findByEmail(request.getOldEmail())
                .orElseThrow(() -> new RuntimeException("Email not found for this user"));

        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Phone does not belong to this user");
        }

        if (!existing.getEmail().equals(request.getOldEmail())) {
            throw new RuntimeException("Old email does not match");
        }

        if (!request.getNewEmail().equals(existing.getEmail())) {
            if (emailDataRepository.findByEmail(request.getNewEmail()).isPresent()) {
                throw new RuntimeException("New email is already taken");
            }
        }

        existing.setEmail(request.getNewEmail());
        EmailData updateEmail = emailDataRepository.save(existing);
        log.info("Email successfully updated to '{}'", updateEmail.getEmail());
        return emailMapper.toDto(updateEmail);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deleteEmail(Long userId, Long emailId) {
        log.info("Deleting email with ID '{}' for user ID: {}", emailId, userId);

        EmailData emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!emailData.getUser().getId().equals(userId)) {
            log.warn("Email with ID '{}' does not belong to user '{}'", emailId, userId);
            throw new RuntimeException("Email does not belong to this user");
        }

        if (emailDataRepository.countByEmail(emailData.getEmail()) <= 1) {
            log.warn("Cannot delete last email '{}'", emailData.getEmail());
            throw new RuntimeException("Cannot delete last email");
        }

        emailDataRepository.delete(emailData);
        log.info("Email '{}' deleted for user ID: {}", emailId, userId);
    }
}