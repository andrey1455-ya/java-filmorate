package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final String messageUserNotFound = "Пользователь не найден";
    private final String messageFilmNotFound = "Фильм не найден";
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film addNewFilm(Film film) {
        return filmStorage.addNewFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.findUserById(userId).orElseThrow(() -> new NotFoundException(messageUserNotFound));
        Film film = filmStorage.findFilmById(filmId).orElseThrow(() -> new NotFoundException(messageFilmNotFound));
        if (user.getLikedFilms().contains(filmId)) {
            log.error("Повторная попытка поставить лайк");
            throw new DuplicatedDataException("Пользователь уже ставил лайк этому фильму");
        }
        user.getLikedFilms().add(filmId);
        log.debug("Пользователь с id = {} добавил фильм \"{}\" в список понравившихся фильмов",
                film.getId(),
                film.getName());
        film.incrementLikes();
        log.debug("Количество лайков у фильма \"{}\" увеличено на 1", film.getName());
        filmStorage.updateLikes(film);
        userStorage.updateLikes(user);
        log.info("Фильму \"{}\" поставил лайк пользователь с id = {}", film.getName(), user.getId());
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        User user = userStorage.findUserById(userId).orElseThrow(() -> new NotFoundException(messageUserNotFound));
        Film film = filmStorage.findFilmById(filmId).orElseThrow(() -> new NotFoundException(messageFilmNotFound));
        if (user.getLikedFilms().contains(filmId)) {
            user.getLikedFilms().remove(filmId);
            log.debug("Пользователь с id = {} удалил фильм \"{}\" из списка понравившихся",
                    film.getId(),
                    film.getName());
            film.decrementLikes();
            log.debug("Количество лайков у фильма \"{}\" уменьшено на 1", film.getName());
            filmStorage.updateLikes(film);
            userStorage.updateLikes(user);
            log.info("Пользователь id = {} удалил лайк фильму \"{}\"", user.getName(), film.getName());
        } else {
            log.error("Попытка удалить лайк фильму, который не был лайкнут");
            throw new ValidationException("Пользователь не ставил лайк этому фильму");
        }
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmStorage
                .findAllFilms()
                .stream()
                .sorted((film1, film2) -> Long.compare(film2.getLike(), film1.getLike()))
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Получена коллекция фильмов начиная с 1 и до {} - включительно и записана в переменную", count);
        if (popularFilms.isEmpty()) {
            log.error("Попытка получить пустой список популярных фильмов");
            throw new NotFoundException("Список популярных фильмов пуст");
        }
        log.info("Пользователем был получен список популярных фильмов  начиная с 1 и до {} - включительно", count);
        return popularFilms;
    }
}
