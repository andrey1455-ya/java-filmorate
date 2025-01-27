package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAllUsers();

    User addNewUser(User user);

    User updateUser(User newUser);

    Optional<User> findUserById(Long id);

    void updateFriends(User user);

    void updateLikes(User user);
}
