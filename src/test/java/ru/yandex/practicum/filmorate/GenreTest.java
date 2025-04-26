package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class})
public class GenreTest {
    private final GenreDbStorage dao;
    private static final int SIZE_GENRE_LIST = 6;

    @DisplayName("Получение всех жанров")
    @Test
    void testGetAllMpa() {
        Collection<Genre> genres = dao.getGenres();
        assertNotNull(genres, "Возвращает пустой объект");
        assertEquals(genres.size(), SIZE_GENRE_LIST, "Количество жанров не совпадает");
    }

    @DisplayName("Получение жанра по id")
    @Test
    void testGetMpaById() {
        Genre genre = dao.getGenreById(1L)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
        assertNotNull(genre, "Возвращает пустой объект");
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }
}
