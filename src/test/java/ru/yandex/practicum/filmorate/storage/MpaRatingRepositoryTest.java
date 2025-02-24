package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRatingRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaRatingRepository.class, MpaRatingRowMapper.class})
class MpaRatingRepositoryTest {
    private final MpaRatingRepository mpaRatingRepository;

    @Test
    void shouldReturnListOfRatingsWhenFindingAllRatings() {
        List<MpaRating> ratings = mpaRatingRepository.getAllRatings();
        assertThat(ratings).hasSize(5);
    }

    @Test
    void shouldReturnRatingWhenFindingByIdWithExistingId() {
        Optional<MpaRating> rating = mpaRatingRepository.getRatingById(1L);
        assertThat(rating).isPresent()
                .hasValueSatisfying(r -> assertThat(r.getName()).isEqualTo("G"));
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindingByIdWithNonExistingId() {
        Optional<MpaRating> rating = mpaRatingRepository.getRatingById(999L);
        assertThat(rating).isEmpty();
    }
}
