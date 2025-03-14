package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.trace("Запущен метод создания");
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Допущена ошибка в электронной почте в методе создания");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Допущена ошибка в логине в методе создания");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null) {
            log.info("Имя не указано, используем логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Допущена ошибка в дате рождения в методе создания");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        log.trace("Пользователю присвоен Id {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Создание успешно завершено");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.trace("Запущен метод редактирования");
        if (newUser.getId() == null) {
            log.error("Не указан Id");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            log.debug("Получен пользователь из коллекции {}", oldUser);
            if (newUser.getEmail() != null) {
                if (!newUser.getEmail().contains("@")) {
                    log.error("Допущена ошибка в электронной почте в методе редактирования");
                    throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
                }
                oldUser.setEmail(newUser.getEmail());
                log.debug("Изменена электронная почта на {}", oldUser.getEmail());
            }
            if (newUser.getLogin() != null) {
                if (newUser.getLogin().contains(" ")) {

                    log.error("Допущена ошибка в логине в методе редактирования");
                    throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                }
                oldUser.setLogin(newUser.getLogin());

                log.debug("Изменен логин на {}", oldUser.getLogin());
            }
            if (newUser.getName() != null) {
                if (!newUser.getName().isBlank()) {
                    oldUser.setName(newUser.getName());
                    log.debug("Изменено имя на {}", oldUser.getName());
                }
            }
            if (newUser.getBirthday() != null) {
                if (newUser.getBirthday().isAfter(LocalDate.now())) {
                    log.error("Допущена ошибка в дате рождения в методе редактирования");
                    throw new ValidationException("Дата рождения не может быть в будущем");
                }
                oldUser.setBirthday(newUser.getBirthday());
                log.debug("Изменена дата рождения на {}", oldUser.getBirthday());
            }
            log.debug("Изменение успешно завершено");
            return oldUser;
        }
        log.error("Допущена ошибка в Id");
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}