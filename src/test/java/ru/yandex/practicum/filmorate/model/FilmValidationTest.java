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
public class FilmValidationTest {

    @Autowired
    private Validator validator;

    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
    }

    @Test
    void shouldFailValidationWhenNameIsNull() {
        film.setName(null);
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenDescriptionIsTooLong() {
        film.setName("Valid Name");
        film.setDescription("A".repeat(201)); // Length greater than 200 characters
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenReleaseDateIsNull() {
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(null); // Not validated as per specification
        film.setDuration(120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenDurationIsNull() {
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenDurationIsNegative() {
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-1); // Invalid duration
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenDurationIsZero() {
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0); // Invalid duration
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        film.setName("Valid Name");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

}