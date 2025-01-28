package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    Film addNewFilm(Film film);

    Film updateFilm(Film newFilm);

    Optional<Film> findFilmById(Long id);

    void updateLikes(Film film);
}
