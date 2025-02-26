package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> addNewUser(@Valid @RequestBody User user) {
        log.info("Запрос на добавление нового пользователя: {}", user);
        UserDto createdUser = userService.addNewUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя с ID: {}", user.getId());
        UserDto updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriendById(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {
        log.info("Запрос на добавление друга с ID: {} пользователю с ID: {}", friendId, id);
        userService.addFriend(friendId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriendById(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {
        log.info("Запрос на удаление друга с ID: {} у пользователя с ID: {}", friendId, id);
        userService.deleteFriend(friendId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserDto>> getFriendsById(@PathVariable Long id) {
        log.info("Запрос на получение друзей пользователя с ID: {}", id);
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId
    ) {
        log.info("Запрос на получение общих друзей пользователей с ID: {} и {}", id, otherId);
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }
}