package com.example.transfermoney.repository;

import com.example.transfermoney.model.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    /**
     * Находит телефон по его номеру.
     */
    Optional<PhoneData> findByPhone(String phone);

    /**
     * Находит все телефоны пользователя по ID.
     */
    List<PhoneData> findAllByUserId(Long userId);

}
