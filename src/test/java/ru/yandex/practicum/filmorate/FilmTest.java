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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({ FilmDbStorage.class,
        FilmRowMapper.class,
        MpaRowMapper.class,
        MpaDbStorage.class,
        GenreDbStorage.class})
public class FilmTest {
    private final FilmDbStorage filmDao;
    private final GenreDbStorage genresDao;
    private static Film film;
    private Long i = 0L;
    private Mpa mpa = Mpa.builder().id(1L).name("G").build();

    @BeforeEach
    public void startTest() {
        film = Film.builder()
                .name("Sopranos" + i)
                .duration(100)
                .description("Description")
                .releaseDate(LocalDate.parse("1996-11-25"))
                .mpa(mpa)
                .build();
        i++;
    }

    @DisplayName("Заполнение основных полей фильма")
    @Test
    void testFilingTheMainDataFilm() {
        Film createFilm = filmDao.create(film);
        Collection<Film> allFilms = filmDao.findAll();
        assertEquals(allFilms.size(), i, "Фильм не был создан");
        assertThat(createFilm).hasFieldOrPropertyWithValue("id", film.getId());
        assertThat(createFilm).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(createFilm).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(createFilm).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(createFilm).hasFieldOrPropertyWithValue("genres", (new LinkedHashSet<>()));
        assertThat(createFilm).hasFieldOrPropertyWithValue("mpa", mpa);

    }

    @DisplayName("Создание фильма с жанрами")
    @Test
    void testCreateFilmWithGenresList() {
        Genre genre1 = genresDao.getGenreById(1L)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
        Genre genre2 = genresDao.getGenreById(2L)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
        Genre genre3 = genresDao.getGenreById(1L)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
        Collection<Genre> genres = Arrays.asList(genre1, genre2, genre3);
        film.setGenres(new LinkedHashSet<>(genres));
        Collection<Genre> resultGenres = Arrays.asList(genre1, genre2);
        Film createFilm = filmDao.create(film);
        Collection<Film> allFilms = filmDao.findAll();
        assertEquals(allFilms.size(), i, "Фильм не был создан");
        assertThat(createFilm).hasFieldOrPropertyWithValue("id", film.getId());
        assertThat(createFilm).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(createFilm).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(createFilm).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertEquals(resultGenres.size(), film.getGenres().size(), "количество жанров не совпадает");
        assertThat(createFilm).hasFieldOrPropertyWithValue("mpa", mpa);
    }

    @DisplayName("Получение фильма по id")
    @Test
    void testGetFilmById() {
        Film createdFilm = filmDao.create(film);
        Film foundFilm = filmDao.findById(createdFilm.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));;
        assertThat(foundFilm).hasFieldOrPropertyWithValue("id", createdFilm.getId());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("name", createdFilm.getName());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("description", createdFilm.getDescription());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("releaseDate",createdFilm.getReleaseDate());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("mpa", mpa);
    }
}
