package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.storage.GenreRepository;
import ru.yandex.practicum.filmorate.storage.LikeRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final LikeRepository likeRepository;
    private final GenreRepository genreRepository;

    public FilmService(FilmRepository filmRepository, LikeRepository likeRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.likeRepository = likeRepository;
        this.genreRepository = genreRepository;
    }

    public List<FilmDto> getAllFilms() {
        log.info("Запрос на получение всех фильмов");
        List<Film> films = filmRepository.getAllFilms();
        enrichFilmsWithGenres(films);
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public Optional<FilmDto> findFilmById(Long id) {
        log.info("Запрос на получение фильма с ID: {}", id);
        Optional<Film> filmOptional = filmRepository.getFilmById(id);
        if (filmOptional.isPresent()) {
            Film film = filmOptional.get();
            enrichFilmWithGenres(film);
            return Optional.of(FilmMapper.mapToFilmDto(film));
        }
        return Optional.empty();
    }

    public FilmDto addNewFilm(Film film) {
        log.info("Запрос на добавление нового фильма: {}", film);
        checkReleaseDate(film);
        Film createdFilm = filmRepository.addNewFilm(film);
        enrichFilmWithGenres(createdFilm);
        return FilmMapper.mapToFilmDto(createdFilm);
    }

    public FilmDto updateFilm(Film film) {
        log.info("Запрос на обновление фильма с ID: {}", film.getId());
        checkReleaseDate(film);
        Film updatedFilm = filmRepository.updateFilm(film);
        enrichFilmWithGenres(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        log.info("Запрос на добавление лайка фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        likeRepository.addLike(filmId, userId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        log.info("Запрос на удаление лайка фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        likeRepository.deleteLike(filmId, userId);
    }

    public List<FilmDto> getPopularFilms(int count) {
        log.info("Запрос на получение {} популярных фильмов", count);
        List<Film> films = filmRepository.getAllFilms();
        enrichFilmsWithGenres(films);
        films.forEach(film -> {
            List<Like> likes = likeRepository.findLikesByFilmId(film.getId());
            film.setLikes(new HashSet<>(likes));
        });
        return films.stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    private void checkReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода не может быть раньше " +
                    "28.12.1895 - даты выхода первого в истории фильма");
        }
    }

    private void enrichFilmWithGenres(Film film) {
        Map<Long, Set<Genre>> genresByFilm = genreRepository.findGenresForFilms(List.of(film.getId()));
        film.setGenres(genresByFilm.getOrDefault(film.getId(), new LinkedHashSet<>()));
    }

    private void enrichFilmsWithGenres(List<Film> films) {
        Map<Long, Set<Genre>> genresByFilm = genreRepository.findGenresForFilms(
                films.stream().map(Film::getId).collect(Collectors.toList())
        );
        films.forEach(film -> film.setGenres(genresByFilm.getOrDefault(film.getId(), new LinkedHashSet<>())));
    }
}