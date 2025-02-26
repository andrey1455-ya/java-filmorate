package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
class UserRepositoryTest {
    private final UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.getAllUsers().forEach(user -> userRepository.delete(String.valueOf(user.getId())));
    }

    @Test
    void shouldSaveUserWhenCreatingValidUser() {
        User user = new User(null, "test@example.com", "Test User", "testuser", LocalDate.of(2000, 1, 1));
        User createdUser = userRepository.addNewUser(user);
        assertThat(createdUser.getId()).isNotNull();
        Optional<User> retrievedUser = userRepository.getUserById(createdUser.getId());
        assertThat(retrievedUser).isPresent()
                .hasValueSatisfying(u -> assertThat(u.getEmail()).isEqualTo("test@example.com"));
    }

    @Test
    void shouldUpdateUserUserWhenUpdatingValidUser() {
        User user = new User(null, "test@example.com", "Test User", "testuser", LocalDate.of(2000, 1, 1));
        User createdUser = userRepository.addNewUser(user);
        createdUser.setEmail("updated@example.com");
        createdUser.setName("Updated User");
        User updatedUser = userRepository.updateUser(createdUser);
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(userRepository.getUserById(updatedUser.getId()))
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u.getName()).isEqualTo("Updated User"));
    }

    @Test
    void shouldReturnListOfUsersWhenFindingAllUsers() {
        User user1 = new User(null, "user1@example.com", "User One", "userone", LocalDate.of(1990, 1, 1));
        User user2 = new User(null, "user2@example.com", "User Two", "usertwo", LocalDate.of(1995, 5, 5));
        userRepository.addNewUser(user1);
        userRepository.addNewUser(user2);
        List<User> users = userRepository.getAllUsers();
        assertThat(users).hasSize(2);
    }
}
