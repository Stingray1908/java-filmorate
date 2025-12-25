package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public ResponseEntity<Collection<User>> getAll() {
        return ResponseEntity.ok().body(users.values());
    }

    @Override
    protected void validate(User user) {
        super.validate(user);

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
            user.setName(user.getLogin());
            log.info("Имя пользователя установлено по логину: {}", user.getLogin());
        }
    }

    @Override
    protected void setId(User user) {
        user.setId(count++);
    }

    @Override
    protected int getId(User user) {
        return user.getId();
    }

    @Override
    protected void putToMap(User user) {
        users.put(user.getId(), user);
    }

    @Override
    protected boolean containsKey(User user) {
        System.out.println(user.getId());
        return users.containsKey(user.getId());
    }
}
