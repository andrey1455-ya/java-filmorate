package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Film {
    Long id;
    @NotNull(message = "Название фильма не может быть null")
    @NotBlank(message = "Название фильма не может быть пустым")
    String name;
    @Size(max = 200, message = "Описание фильма не может быть длиннее 200 символов")
    String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    LocalDate releaseDate;
    @NotNull(message = "Продолжительность фильма не может быть пустой")
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Integer duration;
    @NotNull(message = "MPA рейтинг не может быть пустым")
    MpaRating mpaRating;

    Set<Genre> genres = new HashSet<>();

    private Set<Like> likes = new HashSet<>();
}