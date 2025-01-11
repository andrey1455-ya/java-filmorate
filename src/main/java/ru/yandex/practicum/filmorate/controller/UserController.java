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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long userId = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        if (users.containsValue(newUser)) {
            log.error("Пользователь с email {} уже существует", newUser.getEmail());
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        newUser.setId(getNewUserId());
        if (newUser.getName() == null || newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);
        log.info("Добавлен новый пользователь: {}", newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        if (!users.containsKey(updatedUser.getId())) {
            log.error("Невозможно обновить. Пользователь с id {} не найден.", updatedUser.getId());
            throw new ValidationException("Пользователь с указанным id не найден");
        }
        User existingUser = users.get(updatedUser.getId());
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getLogin() != null) {
            existingUser.setLogin(updatedUser.getLogin());
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getBirthday() != null) {
            existingUser.setBirthday(updatedUser.getBirthday());
        }
        log.info("Обновлены данные по пользователю: {}", existingUser);
        return existingUser;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private Long getNewUserId() {
        return userId++;
    }
}