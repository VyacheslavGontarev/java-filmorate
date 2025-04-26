package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;


    public Collection<Film> findAll() {
        log.trace("Запущен метод поиска фильмов");
        return filmStorage.findAll();
    }

    public Film findById(Long id) {
        log.trace("Запущен метод поиска фильма по id");
        return filmStorage.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    public Film create(Film film) {
        log.trace("Запущен метод создания фильма");
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        log.trace("Запущен метод обновления фильма");
        return filmStorage.update(newFilm);
    }

    public void addLike(Long filmId, Long userId) {
        log.trace("Запущен метод добавления лайка фильму");
        Optional<Film> mayBeFilm = filmStorage.findById(filmId);
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeFilm.isEmpty() || mayBeUser.isEmpty()) {
            throw new NotFoundException("Фильм или пользователь не найдены.");
        }
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.trace("Запущен метод удаления лайка фильма");
        Optional<Film> mayBeFilm = filmStorage.findById(filmId);
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeFilm.isEmpty() || mayBeUser.isEmpty()) {
            throw new NotFoundException("Фильм или пользователь не найдены.");
        }
        likeStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getTopFilms(int count) {
        log.trace("Запущен метод поиска популярных фильмов");
        return filmStorage.getTopFilms(count);
    }
}