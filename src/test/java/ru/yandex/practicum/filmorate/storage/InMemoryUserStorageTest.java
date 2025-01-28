package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InMemoryUserStorageTest {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void initializeUsers() {
        clearAllUsers();
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        inMemoryUserStorage.addNewUser(user);
    }

    private void clearAllUsers() {
        inMemoryUserStorage.getUsers().clear();
    }

    @Test
    public void shouldReturnAllUsers() {
        assertEquals(1, inMemoryUserStorage.findAllUsers().size(), "Изначально должен быть один пользователь");
    }

    @Test
    public void shouldCreateUserWithUniqueEmail() {
        User newUser = new User();
        newUser.setEmail("unique@example.com");
        newUser.setLogin("uniqueLogin");
        newUser.setName("Unique User");
        newUser.setBirthday(LocalDate.of(1990, 2, 15));
        User createdUser = inMemoryUserStorage.addNewUser(newUser);
        assertNotNull(createdUser.getId(), "ID нового пользователя не должен быть null");
        assertEquals(2, inMemoryUserStorage.findAllUsers().size(), "Должно быть два пользователя");
        assertEquals("unique@example.com", createdUser.getEmail(), "Email должен совпадать");
    }

    @Test
    public void shouldThrowExceptionForDuplicateEmail() {
        User duplicateEmailUser = new User();
        duplicateEmailUser.setEmail("test@example.com");
        duplicateEmailUser.setLogin("duplicateLogin");
        duplicateEmailUser.setName("Duplicate User");
        duplicateEmailUser.setBirthday(LocalDate.of(1995, 3, 10));
        assertThrows(DuplicatedDataException.class, () -> inMemoryUserStorage.addNewUser(duplicateEmailUser),
                "Ожидается исключение DuplicatedDataException из-за дублирующегося email");
    }

    @Test
    public void shouldCreateUserWithNoName() {
        User noNameUser = new User();
        noNameUser.setEmail("noname@example.com");
        noNameUser.setLogin("noNameLogin");
        noNameUser.setBirthday(LocalDate.of(1993, 5, 25));
        User createdUser = inMemoryUserStorage.addNewUser(noNameUser);
        assertEquals(noNameUser.getLogin(), createdUser.getName(), "Имя должно быть установлено равным логину");
    }

    @Test
    public void shouldUpdateExistingUser() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updatedLogin");
        updatedUser.setName("Updated User");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));
        User result = inMemoryUserStorage.updateUser(updatedUser);
        assertEquals("updated@example.com", result.getEmail(), "Email должен быть обновлен");
        assertEquals("Updated User", result.getName(), "Имя должно быть обновлено");
        assertEquals("updatedLogin", result.getLogin(), "Логин должен быть обновлен");
    }

    @Test
    public void shouldThrowExceptionForNonExistingUserUpdate() {
        User nonExistingUser = new User();
        nonExistingUser.setId(99L);
        nonExistingUser.setEmail("nonexisting@example.com");
        nonExistingUser.setLogin("nonexistingLogin");
        nonExistingUser.setName("Non-Existing User");
        nonExistingUser.setBirthday(LocalDate.of(1995, 7, 20));
        assertThrows(NotFoundException.class, () -> inMemoryUserStorage.updateUser(nonExistingUser),
                "Ожидается исключение NotFoundException при попытке обновления несуществующего пользователя");
    }

    @Test
    public void shouldThrowExceptionForDuplicateEmailOnUpdate() {
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setLogin("anotherLogin");
        anotherUser.setName("Another User");
        anotherUser.setBirthday(LocalDate.of(1980, 6, 30));
        inMemoryUserStorage.addNewUser(anotherUser);
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("another@example.com");
        updatedUser.setLogin("updatedLogin");
        updatedUser.setName("Updated User");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));
        assertThrows(DuplicatedDataException.class, () -> inMemoryUserStorage.updateUser(updatedUser),
                "Ожидается исключение DuplicatedDataException из-за дублирующегося email");
    }
}
