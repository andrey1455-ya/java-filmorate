package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"name"})
public class Film {
    Long id;
    @NotNull
    @NotBlank
    String name;
    @Size(max = 200, message = "Описание фильма не может быть длиннее 200 символов")
    String description;
    LocalDate releaseDate;
    @NotNull
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Integer duration;
    Long like = 0L;

    public void incrementLikes() {
        like++;
    }

    public void decrementLikes() {
        if (like > 0) {
            like--;
        }
    }
}
