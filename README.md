# java-filmorate
Template repository for Filmorate project.


![Untitled](https://github.com/user-attachments/assets/f966e364-0401-48f4-95a8-6f63a9693cd1)



Схема базы данных для хранения данных о фильмах и пользователях.
Основные таблицы:

- `users` - таблица пользователей.
- `films` - таблица фильмов.
- `genre` - таблица жанров.
- `film_genre` - таблица связи фильмов и жанров.
- `mpa` - таблица возрастных ограничений.
- `friendship` - таблица дружбы пользователей.
- `liked_films` - таблица лайков фильмов пользователями.

Примеры запросов:

1. Получение списка всех пользователей:

```sql
SELECT *
FROM users;
```

2. Получение списка всех фильмов:

```sql
SELECT *
FROM films;
```

3. Получение списка всех жанров:
```sql
SELECT *
FROM genres;
```

4. Получение списка всех возрастных ограничений:
```sql
SELECT *
FROM mpa;
```

5. Получение списка всех друзей пользователя:
```sql
SELECT *
FROM friendship
WHERE user_id = ?;
```

6. Получение списка всех лайков фильмов пользователями:
```sql
SELECT *
FROM liked_films
WHERE film_id = ?;
```

7. Получение списка всех жанров фильма:
```sql
SELECT g.name
FROM film_genre AS fg
INNER JOIN genres g ON film_genre.genre_id = g.genre_id;
WHERE fg.film_id = ?;
```

8. Получение списка всех фильмов пользователя:
```sql
SELECT f.name
FROM films AS f
INNER JOIN liked_films lf ON f.film_id = lf.film_id
INNER JOIN users u ON lf.user_id = u.user_id
WHERE lf.user_id = ?;
```

9. Получение списка всех фильмов, отсортированных по популярности:
```sql
SELECT f.*, COUNT(l.film_id) AS likes_count
FROM films f
LEFT JOIN liked_films l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY likes_count DESC;
```

10. Получение списка всех фильмов, отсортированных по дате релиза:
```sql
SELECT *
FROM films
ORDER BY release_date DESC;
```

Эти запросы могут быть использованы для получения различных данных из базы данных и их дальнейшего использования в приложении.
