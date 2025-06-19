package com.example.transfermoney.repository;

import com.example.transfermoney.model.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    Optional<EmailData> findByEmail(String email);

    /**
     * Находит все email'ы пользователя по ID.
     */
    List<EmailData> findAllByUserId(Long userId);


    Long countByEmail(String email);
}
