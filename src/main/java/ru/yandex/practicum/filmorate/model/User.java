package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;


import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
