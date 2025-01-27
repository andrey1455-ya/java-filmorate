package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public User addNewUser(@Valid @RequestBody User user) {
        return userService.addNewUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/users/{id}/friends/{friend-id}")
    public User addFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friend-id}")
    public User deleteFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{other-id}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable("other-id") Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
