package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class,
        UserRowMapper.class})
public class UserTest {
    private final UserDbStorage dao;
    private static User user;
    private Long i = 0L;

    @BeforeEach
    public void startTest() {
        user = User.builder()
                .name("Igorek" + i)
                .email("igorek" + i + "@yandex.ru")
                .birthday(LocalDate.parse("2001-09-11"))
                .login("login")
                .build();
        i++;
    }

    @DisplayName("Создание пользователя")
    @Test
    void createUser() {
        User createdUser = dao.create(user);
        Collection<User> allUsers = dao.findAll();
        assertEquals(allUsers.size(), i);
        assertThat(createdUser).hasFieldOrPropertyWithValue("id", createdUser.getId());
        assertThat(createdUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(createdUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(createdUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
        assertThat(createdUser).hasFieldOrPropertyWithValue("login", user.getLogin());
    }

    @DisplayName("Получение пользователя по id")
    @Test
    void testGetUserById() {
        User createdUser = dao.create(user);
        User foundUser = dao.findById(createdUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + createdUser.getId() + " не найден"));
        assertThat(foundUser).hasFieldOrPropertyWithValue("id", i);
        assertThat(foundUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(foundUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(foundUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
        assertThat(foundUser).hasFieldOrPropertyWithValue("login", user.getLogin());
    }
}
