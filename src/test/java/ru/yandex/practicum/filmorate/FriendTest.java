package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({ FriendDbStorage.class,
        UserDbStorage.class,
        UserRowMapper.class})
public class FriendTest {
    private final FriendDbStorage friendDao;
    private static User user;
    private static User friend;
    private static User userInBd;
    private static User friendInBd;
    private Long i = 0L;
    @Autowired
    private UserDbStorage userDao;

    @BeforeEach
    public void startTest() {
        user = User.builder()
                .name("Leha" + i)
                .email("leha" + i + "@yandex.ru")
                .birthday(LocalDate.parse("1969-06-09"))
                .login("login" + i)
                .build();
        userInBd = userDao.create(user);
        i++;

        friend = User.builder()
                .name("Artyom" + i)
                .email("artyom" + i + "@yandex.ru")
                .birthday(LocalDate.parse("2011-11-11"))
                .login("login" + i)
                .build();

        friendInBd = userDao.create(friend);
        i++;
    }

    @DisplayName("Отпрaвка запроса на добавление в друзья")
    @Test
    void addFriend() {
        long userId = userInBd.getId();
        long friendId = friendInBd.getId();
        friendDao.addFriends(userId, friendId);
        Collection<User> user1Friends = friendDao.getFriends(userId);
        Collection<User> user2Friends = friendDao.getFriends(friendId);
        assertEquals(user1Friends.size(), 1, "У юзера не добавился друг");
        assertEquals(user2Friends.size(), 0, "У второго пользователя не должны были появиться друзья");
    }

    @DisplayName("Удаление друзей")
    @Test
    void testDeleteFriend() {
        long userId = userInBd.getId();
        long friendId = friendInBd.getId();
        friendDao.addFriends(userId, friendId);
        Collection<User> user1Friends = friendDao.getFriends(userId);
        Collection<User> user2Friends = friendDao.getFriends(friendId);
        assertEquals(user1Friends.size(), 1, "У юзера не добавился друг");
        assertEquals(user2Friends.size(), 0, "У второго пользователя не должны были появиться друзья");

        friendDao.removeFriends(userId, friendId);
        Collection<User> user1FriendsBeforeDelete = friendDao.getFriends(userId);
        assertEquals(user1FriendsBeforeDelete.size(), 0, "друг не удалился");
    }
}