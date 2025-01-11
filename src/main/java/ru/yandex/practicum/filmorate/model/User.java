package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

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
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не должен содержать пробелы")
    String login;
    @NotNull
    @Past
    LocalDate birthday;
}