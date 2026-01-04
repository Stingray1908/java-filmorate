package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        validate(user);
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return List.copyOf(userStorage.getAll());
    }

    User getById(int id) {
        return userStorage.getById(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user == null || friend == null) {
            throw new ValidationException("Пользователь не найден");
        }

        if (userId == friendId) {
            String msg = "Пользователь с ID " + userId + " попытался добавить себя в друзья";
            log.warn(msg);
            throw new ValidationException(msg);
        }

        user.addFriend(friendId);
        friend.addFriend(userId);
        log.debug("Пользователю ID:{} добавлен друг ID:{}", userId, friendId);
    }

    // Удаляем друга по ID
    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user != null && friend != null) {
            user.removeFriend(friendId);
            friend.removeFriend(userId);
        }
    }

    // Получаем список друзей (объекты User)
    public List<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            return List.of();
        }
        return user.getFriendIds().stream()
                .map(userStorage::getById)
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getById(userId);
        User other = userStorage.getById(otherId);

        if (user == null || other == null) {
            return List.of();
        }

        return user.getFriendIds().stream()
                .filter(other.getFriendIds()::contains)
                .map(userStorage::getById)
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    public void validate(User user) {
        if (user == null) {
            log.warn("Получен запрос с null значением пользователя");
            throw new ValidationException("Получен запрос с null значением");
        }

        if (user.getId() == null) user.setId(0);
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Email пользователя не указан");
            throw new ValidationException("Email обязателен для заполнения");
        } else if (!user.getEmail().contains("@")) {
            log.warn("Некорректный формат email: {}", user.getEmail());
            throw new ValidationException("Неверный формат email");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин пользователя не указан");
            throw new ValidationException("Логин обязателен для заполнения");
        } else if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробелы");
            throw new ValidationException("Логин должен быть без пробелов");
        } else if (user.getBirthday() == null ||
                user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения должна быть в прошлом");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            String login = user.getLogin();
            user.setName(login);
            log.info("Имя пользователя установлено по логину: '{}'", login);
        }
    }
}
