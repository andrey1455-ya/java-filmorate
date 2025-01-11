package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidationTest {

    @Autowired
    private Validator validator;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    void shouldFailValidationWhenEmailIsNull() {
        user.setEmail(null);
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        user.setEmail("");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        user.setEmail("invalid-email");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenLoginIsNull() {
        user.setEmail("valid.email@example.com");
        user.setLogin(null);
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenLoginIsBlank() {
        user.setEmail("valid.email@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenLoginContainsSpaces() {
        user.setEmail("valid.email@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenBirthdayIsNull() {
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setBirthday(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenBirthdayIsInFuture() {
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1)); // Setting birthday in the future
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        user.setEmail("valid.email@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

}