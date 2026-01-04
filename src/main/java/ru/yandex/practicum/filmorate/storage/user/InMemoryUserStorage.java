package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int count = 1;

    @Override
    public User create(User user) {
        if (user.getId() != 0) {
            throw new IllegalArgumentException("Нельзя создать пользователя с уже заданным ID");
        }
        user.setId(count++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с id " + user.getId() + " не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public User getById(int id) {
        if (users.get(id) == null) {
            String s = "Пользователь ID:" + id + " не существует в БД";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
