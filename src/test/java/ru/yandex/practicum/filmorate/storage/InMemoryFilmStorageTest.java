package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InMemoryFilmStorageTest {

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void initialize() {
        clearAllFilms();
        Film film = new Film();
        film.setId(1L);
        film.setName("Тестовый фильм");
        film.setDescription("Описание тестового фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);
        inMemoryFilmStorage.addNewFilm(film);
    }

    private void clearAllFilms() {
        inMemoryFilmStorage.getFilms().clear();
    }

    @Test
    public void shouldReturnAllFilms() {
        assertEquals(1, inMemoryFilmStorage.findAllFilms().size(), "Изначально должен быть один фильм.");
    }

    @Test
    public void shouldCreateFilmWithUniqueName() {
        Film newFilm = new Film();
        newFilm.setName("Уникальный фильм");
        newFilm.setDescription("Уникальное описание");
        newFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        newFilm.setDuration(150);
        Film createdFilm = inMemoryFilmStorage.addNewFilm(newFilm);
        assertNotNull(createdFilm.getId(), "ID нового фильма не должен быть null.");
        assertEquals(2, inMemoryFilmStorage.findAllFilms().size(), "Должно быть два фильма.");
        assertEquals("Уникальный фильм", createdFilm.getName(), "Название должно совпадать.");
    }

    @Test
    public void shouldThrowExceptionForDuplicateFilmName() {
        Film duplicateFilm = new Film();
        duplicateFilm.setName("Тестовый фильм");
        duplicateFilm.setDescription("Другое описание");
        duplicateFilm.setReleaseDate(LocalDate.of(2021, 1, 1));
        duplicateFilm.setDuration(100);
        assertThrows(DuplicatedDataException.class, () -> inMemoryFilmStorage.addNewFilm(duplicateFilm),
                "Ожидается исключение DuplicatedDataException из-за дублирующегося названия.");
    }

    @Test
    public void shouldThrowExceptionForInvalidReleaseDate() {
        Film invalidFilm = new Film();
        invalidFilm.setName("Некорректный фильм");
        invalidFilm.setDescription("Некорректное описание");
        invalidFilm.setReleaseDate(LocalDate.of(1890, 1, 1)); // Дата до 28.12.1895
        invalidFilm.setDuration(90);
        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.addNewFilm(invalidFilm),
                "Ожидается исключение ValidationException из-за некорректной даты выхода.");
    }

    @Test
    public void shouldUpdateExistingFilm() {
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Обновленный фильм");
        updatedFilm.setDescription("Обновленное описание");
        updatedFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        updatedFilm.setDuration(130);
        Film result = inMemoryFilmStorage.updateFilm(updatedFilm);
        assertEquals("Обновленный фильм", result.getName(), "Название должно быть обновлено.");
        assertEquals("Обновленное описание", result.getDescription(), "Описание должно быть обновлено.");
        assertEquals(130, result.getDuration(), "Длительность должна быть обновлена.");
    }

    @Test
    public void shouldThrowExceptionForNonExistingFilmUpdate() {
        Film nonExistingFilm = new Film();
        nonExistingFilm.setId(99L);
        nonExistingFilm.setName("Несуществующий фильм");
        nonExistingFilm.setDescription("Несуществующее описание");
        nonExistingFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        nonExistingFilm.setDuration(100);
        assertThrows(NotFoundException.class, () -> inMemoryFilmStorage.updateFilm(nonExistingFilm),
                "Ожидается исключение NotFoundException при попытке обновления несуществующего фильма.");
    }

    @Test
    public void shouldThrowExceptionForDuplicateNameOnUpdate() {
        Film anotherFilm = new Film();
        anotherFilm.setName("Другой фильм");
        anotherFilm.setDescription("Другое описание");
        anotherFilm.setReleaseDate(LocalDate.of(2023, 2, 1));
        anotherFilm.setDuration(140);
        inMemoryFilmStorage.addNewFilm(anotherFilm);
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Другой фильм"); // Дублируемое название
        updatedFilm.setDescription("Обновленное описание");
        updatedFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        updatedFilm.setDuration(130);
        assertThrows(DuplicatedDataException.class, () -> inMemoryFilmStorage.updateFilm(updatedFilm),
                "Ожидается исключение DuplicatedDataException из-за дублирующегося названия.");
    }
}
