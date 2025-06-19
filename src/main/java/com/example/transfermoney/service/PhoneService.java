package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.phone.PhoneCreateRequest;
import com.example.transfermoney.model.dto.phone.PhoneDto;
import com.example.transfermoney.model.dto.phone.PhoneUpdateRequest;
import com.example.transfermoney.model.entity.PhoneData;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.model.mapper.PhoneMapper;
import com.example.transfermoney.repository.PhoneDataRepository;
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
public class PhoneService {

    private final PhoneDataRepository phoneDataRepository;
    private final UserRepository userRepository;
    private final PhoneMapper phoneMapper;

    public List<PhoneDto> getPhonesByUserId(Long userId) {
        log.debug("Fetching phones for user ID: {}", userId);
        return phoneDataRepository.findAllByUserId(userId).stream()
                .map(phone -> new PhoneDto(phone.getId(), phone.getPhone()))
                .toList();
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public PhoneDto addPhone(Long userId, PhoneCreateRequest request) {
        log.info("Adding new phone '{}' for user ID: {}", request.getPhone(), userId);
        if (phoneDataRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone is already taken");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PhoneData phoneData = new PhoneData();
        phoneData.setUser(user);
        phoneData.setPhone(request.getPhone());
        PhoneData savedPhoneData = phoneDataRepository.save(phoneData);
        log.info("Phone '{}' successfully added for user ID: {}", savedPhoneData.getPhone(), userId);
        return phoneMapper.toDto(savedPhoneData);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#request.userId")
    public PhoneDto updatePhone(Long userId, PhoneUpdateRequest request) {
        log.info("Updating phone ID: {} from '{}' to '{}'", request.getId(), request.getOldPhone(), request.getNewPhone());
        PhoneData existing = phoneDataRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Phone not found"));

        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Phone does not belong to this user");
        }

        if (!existing.getPhone().equals(request.getOldPhone())) {
            throw new RuntimeException("Old phone does not match");
        }

        if (!request.getNewPhone().equals(existing.getPhone())) {
            if (phoneDataRepository.findByPhone(request.getNewPhone()).isPresent()) {
                throw new RuntimeException("New phone is already taken");
            }
        }

        existing.setPhone(request.getNewPhone());
        PhoneData updatedPhoneData = phoneDataRepository.save(existing);
        log.info("Phone ID: {} successfully updated to '{}'", updatedPhoneData.getId(), updatedPhoneData.getPhone());
        return phoneMapper.toDto(updatedPhoneData);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deletePhone(Long userId, Long phoneId) {
        log.info("Deleting phone ID: {} for user ID: {}", phoneId, userId);
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found"));

        if (!phoneData.getUser().getId().equals(userId)) {
            throw new RuntimeException("Phone does not belong to this user");
        }

        if (phoneDataRepository.findAllByUserId(userId).size() <= 1) {
            throw new RuntimeException("Cannot delete last phone");
        }

        phoneDataRepository.deleteById(phoneId);
        log.info("Phone ID: {} successfully deleted", phoneId);
    }
}
