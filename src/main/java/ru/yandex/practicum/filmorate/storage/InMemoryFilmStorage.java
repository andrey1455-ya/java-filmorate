package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final String messageFilmNameDuplicate = "Название фильма уже используется";
    @Getter
    protected final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        log.info("Коллекция фильмов отправлена по запросу");
        return films.values();
    }

    @Override
    public Film addNewFilm(Film newFilm) {
        for (Film value : films.values()) {
            if (newFilm.getName().equals(value.getName())) {
                log.error("При добавлении указано существующее название фильма");
                throw new DuplicatedDataException(messageFilmNameDuplicate);
            }
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При добавлении фильма указана дата раньше 28.12.1895");
            throw new ValidationException("Дата выхода не может быть раньше даты выхода первого в истории фильма");
        }
        newFilm.setId(getNextId());
        log.debug("Фильму \"{}\" присвоен id = {}", newFilm.getName(), newFilm.getId());
        films.put(newFilm.getId(), newFilm);
        log.info("Добавлен фильм с id = {}", newFilm.getId());
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("При обновлении не указан Id фильма");
            throw new ValidationException("Должен быть указан Id фильма");

        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("При обновлении фильма указана дата раньше 28.12.1895");
            throw new ValidationException("Дата выхода не может быть раньше даты выхода первого в истории фильма");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.trace("Создали переменную старого фильма для сравнения с новой");
            if (newFilm.getName() != null) {
                for (Film value : films.values()) {
                    if (newFilm.getName().equals(value.getName())) {
                        log.error("Попытка занять уже используемое название при обновлении");
                        throw new DuplicatedDataException(messageFilmNameDuplicate);
                    }
                }
            }
            oldFilm.setName(newFilm.getName());
            log.debug("Фильму с id = {} установлено имя - {}", newFilm.getId(), newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            log.debug("Фильму с id = {} установлено описание - {}", newFilm.getId(), newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.debug("Фильму с id = {} установлена дата выхода - {}", newFilm.getId(), newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.debug("Фильму с id = {} установлена длительность - {}", newFilm.getId(), newFilm.getDuration());
            log.info("Фильм \"{}\" с id = {}  - обновлен", newFilm.getName(), newFilm.getId());
            return oldFilm;
        }
        log.error("Попытка получить фильм с несуществующим id = {}", newFilm.getId());
        throw new NotFoundException(String.format("Фильм с id = %d  - не найден", newFilm.getId()));
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        log.debug("Cоздали новый id = {} ", currentMaxId);
        return ++currentMaxId;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        log.debug("Выполняем поиск фильма в коллекции фильмов по id = {} ", id);
        return Optional.ofNullable(films.get(id));
    }

    public void updateLikes(Film film) {
        if (films.containsKey(film.getId())) {
            Film existingFilm = films.get(film.getId());
            existingFilm.setLike(film.getLike());
            log.debug("Фильм с id = {} обновлён с новым количеством лайков", film.getId());
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }
}
