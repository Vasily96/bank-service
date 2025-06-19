package com.example.transfermoney.utils;

import com.example.transfermoney.model.entity.EmailData;
import com.example.transfermoney.model.entity.PhoneData;
import com.example.transfermoney.model.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<User> nameLike(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%");
    }

    public static Specification<User> emailEqualTo(String email) {
        return (root, query, cb) -> {
            if (email == null) return null;
            Join<User, EmailData> emails = root.join("emails");
            return cb.equal(emails.get("email"), email);
        };
    }

    public static Specification<User> phoneEqualTo(String phone) {
        return (root, query, cb) -> {
            if (phone == null) return null;
            Join<User, PhoneData> phones = root.join("phones");
            return cb.equal(phones.get("phone"), phone);
        };
    }

    public static Specification<User> dateOfBirthAfter(LocalDate dateOfBirth) {
        return (root, query, cb) ->
                dateOfBirth == null ? null :
                        cb.greaterThan(root.get("dateOfBirth"), dateOfBirth);
    }
}
