package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ru.yandex.practicum.filmorate.controller.serializer.DurationSerializer;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    //private ArrayList<Genre> genres = new ArrayList<>();
    //private MPA mpa;
    @JsonSerialize(using = DurationSerializer.class)
    Duration duration;
    private Set<Long> likes = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
