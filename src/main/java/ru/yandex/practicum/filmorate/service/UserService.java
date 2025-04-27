package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;

    public Collection<User> findAll() {
        log.error("Запущен метод поиска пользователя");
        return userStorage.findAll();
    }

    public User findById(Long id) {
        log.trace("Запущен метод поиска пользователя по id");
        return userStorage.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    public User create(User user) {
        log.error("Запущен метод создания пользователя");
        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.trace("Запущен метод обновления пользователя");
        return userStorage.update(newUser);
    }

    public void addFriend(Long userId, Long friendId) {
        log.trace("Запущен метод добавления друга пользователя");
        Optional<User> mayBeUser = userStorage.findById(userId);
        Optional<User> mayBeFriend = userStorage.findById(friendId);

        if (mayBeUser.isEmpty() || mayBeFriend.isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        friendStorage.addFriends(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        log.trace("Запущен метод удаления друга пользователя");
        Optional<User> mayBeUser = userStorage.findById(userId);
        Optional<User> mayBeFriend = userStorage.findById(friendId);

        if (mayBeUser.isEmpty() || mayBeFriend.isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        friendStorage.removeFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        log.trace("Запущен метод поиска друзей пользователя");
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return friendStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        log.trace("Запущен метод поиска общих друзей пользователя");
        Optional<User> mayBeUser = userStorage.findById(userId);
        Optional<User> mayBeOtherUser = userStorage.findById(otherUserId);

        if (mayBeUser.isEmpty() || mayBeOtherUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return friendStorage.getCommonFriends(userId, otherUserId);

    }
}