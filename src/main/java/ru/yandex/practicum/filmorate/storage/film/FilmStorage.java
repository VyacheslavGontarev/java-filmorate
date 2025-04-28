package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;


public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    Optional<Film> findById(Long id) throws ValidationException;

    Collection<Film> getTopFilms(int limit) throws InternalServerException;
}
