package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public UserService(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public List<UserDto> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        return userRepository.getUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", id)));
    }

    public UserDto addNewUser(User user) {
        log.info("Запрос на добавление нового пользователя: {}", user);
        checkName(user);
        User createdUser = userRepository.addNewUser(user);
        return UserMapper.mapToUserDto(createdUser);
    }

    public UserDto updateUser(User newUser) {
        log.info("Запрос на обновление пользователя с ID: {}", newUser.getId());
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (userRepository.getUserById(newUser.getId()).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", newUser.getId()));
        }
        checkName(newUser);
        User updatedUser = userRepository.updateUser(newUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public List<UserDto> getFriends(Long userId) {
        log.info("Запрос на получение друзей пользователя с ID: {}", userId);
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
        return userRepository.getFriends(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public void addFriend(Long senderId, Long receiverId) {
        log.info("Запрос на добавление друга с ID: {} пользователю с ID: {}", receiverId, senderId);
        friendshipRepository.addFriend(senderId, receiverId);
    }

    public void deleteFriend(Long senderId, Long receiverId) {
        log.info("Запрос на удаление друга с ID: {} у пользователя с ID: {}", receiverId, senderId);
        friendshipRepository.deleteFriend(senderId, receiverId);
    }

    public List<UserDto> getCommonFriends(Long userId, Long friendId) {
        log.info("Запрос на получение общих друзей пользователей с ID: {} и {}", userId, friendId);
        return userRepository.getCommonFriends(userId, friendId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}