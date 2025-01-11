package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 1;

    @PostMapping
    public Film adNewFilm(@Valid @RequestBody Film newFilm) {
        if (films.containsValue(newFilm)) {
            log.error("Фильм {} уже существует", newFilm);
            throw new ValidationException("Фильм с таким названием уже существует");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата выхода {} раньше 28.12.1895", newFilm.getName());
            throw new ValidationException("Дата выхода не может быть раньше даты выхода первого в истории фильма");
        }
        newFilm.setId(getNewFilmId());
        films.put(newFilm.getId(), newFilm);
        log.info("Добавлен новый фильм: {}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            log.error("Обновление не удалось. Фильм с id {} не найден", newFilm.getId());
            throw new ValidationException("Фильм с указанным id не найден");
        }
        Film oldFilm = films.get(newFilm.getId());
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null && newFilm.getDuration() > 0) {
            oldFilm.setDuration(newFilm.getDuration());
        }
        log.info("Обновлены данные по фильму: {}", oldFilm);
        return oldFilm;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    private Long getNewFilmId() {
        return filmId++;
    }
}