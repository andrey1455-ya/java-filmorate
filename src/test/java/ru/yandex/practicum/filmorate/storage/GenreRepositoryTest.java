package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreRowMapper.class})
class GenreRepositoryTest {
    private final GenreRepository genreRepository;

    @Test
    void shouldReturnListOfGenresWhenGetAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();
        assertThat(genres).hasSize(6);
    }

    @Test
    void shouldReturnGenreWhenFindByIdWithExistingId() {
        Optional<Genre> genre = genreRepository.getGenreById(1L);
        assertThat(genre).isPresent()
                .hasValueSatisfying(g -> assertThat(g.getName()).isEqualTo("Комедия"));
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdWithNonExistingId() {
        Optional<Genre> genre = genreRepository.getGenreById(999L);
        assertThat(genre).isEmpty();
    }
}
