package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;


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
        Film film = mayBeFilm.get();
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.trace("Запущен метод удаления лайка фильма");
        Optional<Film> mayBeFilm = filmStorage.findById(filmId);
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeFilm.isEmpty() || mayBeUser.isEmpty()) {
            throw new NotFoundException("Фильм или пользователь не найдены.");
        }
        Film film = mayBeFilm.get();
        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            throw new NotFoundException("Лайк пользователя не найден.");
        }
    }

    public List<Film> getTopFilms(int count) {
        log.trace("Запущен метод подбора самых оценённых фильмов");
        return filmStorage.findAll().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}