package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = { "email" })
public class User {
    Long id;
    @NotNull
    @NotBlank
    @Email
    String email;
    String name;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не может содержать пробелы")
    String login;
    @NotNull
    @Past
    LocalDate birthday;
    Set<Long> likedFilms = new HashSet<>();
    Set<Long> friends = new HashSet<>();
}
