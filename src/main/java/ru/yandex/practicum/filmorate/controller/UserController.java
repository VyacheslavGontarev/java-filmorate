package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Запущен метод поиска пользователя");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {

        log.trace("Запущен метод поиска пользователя по id");
        return userService.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.trace("Запущен метод создания пользователя");
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.trace("Запущен метод обновления пользователя");
        return userService.update(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.trace("Запущен метод добавления друга пользователя");
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.trace("Запущен метод удаления друга пользователя");
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Set<User> getFriends(@PathVariable Long userId) {
        log.trace("Запущен метод поиска друзей пользователя");
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public Set<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherUserId) {
        log.trace("Запущен метод поиска общих друзей пользователей");
        return userService.getCommonFriends(userId, otherUserId);
    }
}