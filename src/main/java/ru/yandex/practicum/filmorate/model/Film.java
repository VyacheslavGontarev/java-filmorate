package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.LinkedHashSet;


@Builder
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;
    private int duration;

}
