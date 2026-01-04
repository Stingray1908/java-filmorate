package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        log.info("Получение запроса на создание пользователя: {}", user);
        User createdUser = userService.createUser(user);
        log.info("Пользователь с id:{} успешно создан и добавлен в таблицу", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        log.info("Получение запроса на обновление пользователя: {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("Пользователь с id:{} успешно обновлён в таблице", updatedUser.getId());
        return ResponseEntity.ok().body(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("Получение запроса на получение списка всех пользователей");
        List<User> users = userService.getAllUsers();
        log.info("Получено {} пользователей из сервиса", users.size());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(
            @PathVariable int userId,
            @PathVariable int friendId) {
        log.info("Получение запроса на добавление друга: userId={}, friendId={}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("Друг с id:{} успешно добавлен в друзья пользователю с id:{}", friendId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable int userId,
            @PathVariable int friendId) {
        log.info("Получение запроса на удаление друга: userId={}, friendId={}", userId, friendId);
        userService.removeFriend(userId, friendId);
        log.info("Друг с id:{} успешно удалён из друзей пользователя с id:{}", friendId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable int userId) {
        log.info("Получение запроса на список друзей пользователя с id:{}", userId);
        List<User> friends = userService.getFriends(userId);
        log.info("Для пользователя с id:{} получено {} друзей", userId, friends.size());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(
            @PathVariable int userId,
            @PathVariable int otherId) {
        log.info("Получение запроса на общие друзья: userId={}, otherId={}", userId, otherId);
        List<User> commonFriends = userService.getCommonFriends(userId, otherId);
        log.info("Для пользователей с id:{} и id:{} найдено {} общих друзей", userId, otherId, commonFriends.size());
        return ResponseEntity.ok(commonFriends);
    }
}
