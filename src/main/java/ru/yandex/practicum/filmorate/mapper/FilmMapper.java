package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        log.debug("Маппинг Film в FilmDto: {}", film);

        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        if (film.getMpaRating() != null) {
            MpaRatingDto mpaDto = new MpaRatingDto();
            mpaDto.setId(film.getMpaRating().getId());
            mpaDto.setName(film.getMpaRating().getName()); // Подставляем имя рейтинга
            dto.setMpa(mpaDto);
        }

        if (film.getGenres() != null) {
            Set<GenreDto> genreDtos = film.getGenres().stream()
                    .map(FilmMapper::toGenreDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new)); // Сохраняем порядок
            dto.setGenres(genreDtos);
        }

        if (film.getLikes() != null) {
            dto.setLikes(film.getLikes());
        }

        return dto;
    }

    public static Film mapToFilm(FilmDto dto) {
        log.debug("Маппинг FilmDto в Film: {}", dto);

        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());

        if (dto.getMpa() != null) {
            MpaRating mpaRating = new MpaRating();
            mpaRating.setId(dto.getMpa().getId());
            mpaRating.setName(dto.getMpa().getName()); // Сохраняем имя рейтинга
            film.setMpaRating(mpaRating);
        }

        if (dto.getGenres() != null) {
            Set<Genre> genres = dto.getGenres().stream()
                    .map(FilmMapper::toGenre)
                    .collect(Collectors.toCollection(LinkedHashSet::new)); // Сохраняем порядок
            film.setGenres(genres);
        }

        return film;
    }

    private static GenreDto toGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }

    private static Genre toGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName()); // Сохраняем имя жанра
        return genre;
    }
}