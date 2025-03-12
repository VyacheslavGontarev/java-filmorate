package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.trace("Запущен метод создания");
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Допущена ошибка в названии в методе создания");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Допущена ошибка в описании в методе создания");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Допущена ошибка в дате выхода в методе создания");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.error("Допущена ошибка в продолжительности в методе создания");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        film.setId(getNextId());
        log.trace("Фильму присвоен Id {}", film.getId());
        films.put(film.getId(), film);
        log.debug("Создание успешно завершено");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.trace("Запущен метод редактирования");
        if (newFilm.getId() == null) {
            log.error("Не указан Id");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.debug("Получен фильм из коллекции {}", oldFilm);
            if (newFilm.getName() != null) {
                if (newFilm.getName().isBlank()) {
                    log.error("Допущена ошибка в названии в методе редактирования");
                    throw new ValidationException("Название фильма не может быть пустым");
                }
                oldFilm.setName(newFilm.getName());
                log.debug("Изменено название на {}", oldFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                if (newFilm.getDescription().length() > 200) {
                    log.error("Допущена ошибка в описании в методе редактирования");
                    throw new ValidationException("Максимальная длина описания — 200 символов");
                }
                oldFilm.setDescription(newFilm.getDescription());
                log.debug("Изменено описание на {}", oldFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) {
                if (newFilm.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                    log.error("Допущена ошибка в дате выхода в методе редактирования");
                    throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
                }
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                log.debug("Изменена дата выхода на {}", oldFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                if (newFilm.getDuration().isNegative() || newFilm.getDuration().isZero()) {
                    log.error("Допущена ошибка в продолжительности в методе редактирования");
                    throw new ValidationException("Продолжительность фильма должна быть положительной");
                }
                oldFilm.setDuration(newFilm.getDuration());
                log.debug("Изменена продолжительность на {}", oldFilm.getDuration());
            }
            log.debug("Изменение успешно завершено");
            return oldFilm;
        }

        log.error("Допущена ошибка в Id");
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
