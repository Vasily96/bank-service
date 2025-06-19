package com.example.transfermoney.service;

import com.example.transfermoney.model.dto.user.UserDto;
import com.example.transfermoney.model.dto.user.UserSearchCriteria;
import com.example.transfermoney.model.entity.User;
import com.example.transfermoney.model.mapper.UserMapper;
import com.example.transfermoney.repository.UserRepository;
import com.example.transfermoney.utils.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;


    @Cacheable("users")
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.info("Fetching user with ID '{}'", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return mapper.toDto(user);
    }


    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        log.info("Deleting user with ID '{}'", id);
        userRepository.deleteById(id);
        log.info("User with ID '{}' deleted successfully", id);
    }


    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(UserSearchCriteria criteria, Pageable pageable) {
        log.info("Searching users with criteria: {}, pageable: {}", criteria, pageable);
        Specification<User> spec = (root, query, builder) -> null;

        if (criteria.getName() != null) {
            spec = spec.and(UserSpecification.nameLike(criteria.getName()));
        }
        if (criteria.getEmail() != null) {
            spec = spec.and(UserSpecification.emailEqualTo(criteria.getEmail()));
        }
        if (criteria.getPhone() != null) {
            spec = spec.and(UserSpecification.phoneEqualTo(criteria.getPhone()));
        }
        if (criteria.getDateOfBirth() != null) {
            spec = spec.and(UserSpecification.dateOfBirthAfter(criteria.getDateOfBirth()));
        }
        Page<User> usersPage = userRepository.findAll(spec, pageable);
        Page<UserDto> dtoPage = usersPage.map(mapper::toDto);
        log.info("Found {} users matching criteria", dtoPage.getTotalElements());
        return dtoPage;
    }
}
