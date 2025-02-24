package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class})
class FilmRepositoryTest {
    private final FilmRepository filmRepository;

    @BeforeEach
    void setUp() {
        filmRepository.getAllFilms().forEach(film -> filmRepository.delete(String.valueOf(film.getId())));
    }

    @Test
    void shouldSaveFilmWhenCreateValidFilm() {
        Film film = new Film(null, "New Film", "New Description",
                LocalDate.of(2023, 3, 1), 150,
                new MpaRating(1L, "G"), Set.of(), Set.of());
        Film savedFilm = filmRepository.addNewFilm(film);
        assertThat(savedFilm.getId()).isNotNull();
        assertThat(filmRepository.getFilmById(savedFilm.getId())).isPresent();
    }

    @Test
    void shouldReturnListOfFilmsWhenGetAllFilms() {
        Film film1 = new Film(null, "Film One", "Description One",
                LocalDate.of(2023, 1, 1), 120,
                new MpaRating(1L, "G"), Set.of(), Set.of());
        Film film2 = new Film(null, "Film Two", "Description Two",
                LocalDate.of(2023, 2, 1), 90,
                new MpaRating(2L, "PG"), Set.of(), Set.of());
        filmRepository.addNewFilm(film1);
        filmRepository.addNewFilm(film2);
        List<Film> films = filmRepository.getAllFilms();
        assertThat(films).hasSize(2);
    }
}
