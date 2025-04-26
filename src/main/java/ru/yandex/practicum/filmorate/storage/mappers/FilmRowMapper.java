package ru.yandex.practicum.filmorate.storage.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@Slf4j
@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
             try {
                 log.error("here");
                 Film film = Film.builder()
                         .id(rs.getLong("id"))
                         .name(rs.getString("name"))
                         .description(rs.getString("description"))
                         .releaseDate(rs.getDate("release_date").toLocalDate())
                         .genres(new LinkedHashSet<>())
                         .mpa((Mpa.builder()
                                 .id(rs.getLong("mpa_id"))
                                 .name(rs.getString("name_mpa"))
                                 .build()))
                         .duration(rs.getInt("duration"))
                         .build();
                 return film;
             } catch (Exception e) {
                 throw new NotFoundException("Ресурс не найден: " + e.getMessage());
             }
    }
}
