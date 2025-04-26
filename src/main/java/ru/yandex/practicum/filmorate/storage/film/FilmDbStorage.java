package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import lombok.extern.slf4j.Slf4j;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Autowired
    protected JdbcTemplate jdbc;

    private static final String FIND_BY_ID_QUERY = """
    SELECT f.id, f.name, f.description, f.release_date, f.duration,
     m.mpa_id, m.name AS name_mpa
     FROM films AS f
     LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id
     WHERE f.id = ?
    """;
    private static final String FIND_ALL_QUERY = """
     SELECT f.id, f.name, f.description, f.release_date, f.duration,
     m.mpa_id, m.name AS name_mpa
     FROM films AS f
     LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id
     """;
    private static final String INSERT_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?);
            """;
    private static final String UPDATE_QUERY = """
            UPDATE films SET name = ?, description = ?, release_date = ?,
            duration = ?, mpa_id = ?  WHERE id = ?
            """;

    @Override
    public Optional<Film> findById(Long id) {
        try {
            Film result = jdbc.queryForObject(FIND_BY_ID_QUERY, new FilmRowMapper(), id);
            String genreSql = """
                    SELECT g.genre_id, g.name FROM film_genre AS fg
                    LEFT JOIN films AS f ON f.id = fg.film_id
                    LEFT JOIN genre AS g ON fg.genre_id = g.genre_id
                    WHERE f.id = ?
                    """;
            List<Genre> genre = jdbc.query(genreSql, new GenreRowMapper(), id);
            result.setGenres(new LinkedHashSet<>(genre));
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Film> findAll() {
        log.error("findAll");
        List<Film> result = jdbc.query(FIND_ALL_QUERY, new FilmRowMapper());
        log.error("коллекция собрана");
        String genreSql = """
                SELECT g.genre_id, g.name FROM film_genre AS fg
                LEFT JOIN films AS f ON f.id = fg.film_id
                LEFT JOIN genre AS g ON fg.genre_id = g.genre_id WHERE f.id = ?
                """;

        for (Film film : result) {
            long id = film.getId();
            List<Genre> genre = jdbc.query(genreSql, new GenreRowMapper(), id);
            film.setGenres(new LinkedHashSet<>(genre));
        }
        return result;
    }

    @Override
    public Film create(Film film) {
        log.error("Запущен метод создания фильма");
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
        if (film.getDuration() <= 0) {
            log.error("Допущена ошибка в продолжительности в методе создания");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            try {
                jdbc.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
                    statement.setString(1, film.getName());
                    statement.setString(2, film.getDescription());
                    statement.setDate(3, Date.valueOf(film.getReleaseDate()));
                    statement.setInt(4, film.getDuration());
                    if (film.getMpa() != null) {
                        statement.setLong(5, film.getMpa().getId());
                    } else {
                        statement.setNull(5, Types.INTEGER);
                    }
                    return statement;
                }, keyHolder);
            } catch (Exception e) {
                throw new NotFoundException("MPA not found");
            }
            film.setId(keyHolder.getKey().longValue());
            updateGenres(film);
            return film;
        } catch (InternalServerException ex) {
            log.error("Ошибка при создании пользователя: {}", ex.getMessage());
            throw new RuntimeException("Не удалось создать пользователя");
        }
    }

    @Override
    public Film update(Film newFilm) {
        log.trace("Запущен метод редактирования фильма");
        if (newFilm.getId() == null) {
            log.error("Не указан Id");
            throw new ValidationException("Id должен быть указан");
        }
        Optional<Film> mayBeFilm = findById(newFilm.getId());
        if (mayBeFilm.isPresent()) {
            Film oldFilm = mayBeFilm.get();
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

                if (newFilm.getDuration() > 0) {
                    oldFilm.setDuration(newFilm.getDuration());
                    log.debug("Изменена продолжительность на {}", oldFilm.getDuration());
                }
            if (newFilm.getMpa() != null) {
                oldFilm.setMpa(newFilm.getMpa());
                log.debug("Изменён id рейтинга на {}", oldFilm.getMpa());
            }
            int rowsUpdated = jdbc.update(UPDATE_QUERY, oldFilm.getName(), oldFilm.getDescription(),
                    oldFilm.getReleaseDate(), oldFilm.getDuration(), oldFilm.getMpa().getId(), oldFilm.getId());
            if (rowsUpdated == 0) {
                throw new InternalServerException("Не удалось обновить данные");
            }
            String sqlQueryForDeleteGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
            jdbc.update(sqlQueryForDeleteGenres, oldFilm.getId());

            updateGenres(oldFilm);
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private void updateGenres(Film film) {
        log.error("запущен метод обновления жанров");
        try {
        if (film.getGenres() != null) {
            String sqlQueryForGenres = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) " +
                    "VALUES (?, ?)";
            jdbc.batchUpdate(
                    sqlQueryForGenres, film.getGenres(), film.getGenres().size(),
                    (ps, genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genre.getId());
                    });
        } else film.setGenres(new LinkedHashSet<>());
        } catch (Exception e) {
            throw new NotFoundException("Genre not found");
        }
    }

    @Override
    public Collection<Film> getTopFilms(int limit) {

        String sql = FIND_ALL_QUERY + "LEFT JOIN film_likes AS fl ON f.id = fl.film_id\n" +
                "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, m.mpa_id, m.name\n" +
                "ORDER BY COUNT(fl.film_id) DESC\n" +
                "LIMIT ?";
        return jdbc.query(sql, new FilmRowMapper(), limit);
    }
}
