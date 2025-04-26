package ru.yandex.practicum.filmorate.storage.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import lombok.extern.slf4j.Slf4j;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    @Autowired
    protected JdbcTemplate jdbc;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, " +
            "birthday = ?  WHERE id = ?";

    @Override
    public Optional<User> findById(Long id) {
        try {
            User result = jdbc.queryForObject(FIND_BY_ID_QUERY, new UserRowMapper(), id);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

   @Override
    public Collection<User> findAll() {
        log.error("findAll");
         Collection result = jdbc.query(FIND_ALL_QUERY, new UserRowMapper());
        return result;
    }

   @Override
    public User create(User user) {
         if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setDate(4, Date.valueOf(user.getBirthday()));
                return ps;
            }, keyHolder);

            Long userId = keyHolder.getKey().longValue();
            user.setId(userId);
            return user;
        } catch (InternalServerException ex) {
            log.error("Ошибка при создании пользователя: {}", ex.getMessage());
            throw new RuntimeException("Не удалось создать пользователя");
        }
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        Optional<User> mayBeUser = findById(newUser.getId());
        if (mayBeUser.isPresent()) {
            User oldUser = mayBeUser.get();
            if (newUser.getEmail() != null) {
                if (!newUser.getEmail().contains("@")) {
                    throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
                }
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getLogin() != null) {
                if (newUser.getLogin().contains(" ")) {
                    throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                }
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getName() != null) {
                if (!newUser.getName().isBlank()) {
                    oldUser.setName(newUser.getName());
                }
            }
            if (newUser.getBirthday() != null) {
                if (newUser.getBirthday().isAfter(LocalDate.now())) {
                    throw new ValidationException("Дата рождения не может быть в будущем");
                }
                oldUser.setBirthday(newUser.getBirthday());
            }
            int rowsUpdated = jdbc.update(UPDATE_QUERY, oldUser.getEmail(), oldUser.getLogin(), oldUser.getName(),
                    oldUser.getBirthday(),oldUser.getId());
            if (rowsUpdated == 0) {
                throw new InternalServerException("Не удалось обновить данные");
            }
            return oldUser;
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}