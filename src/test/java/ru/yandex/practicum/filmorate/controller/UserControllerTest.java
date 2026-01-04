package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    private User user;
    private Executable validExecutor;
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);


    @BeforeEach
    public void beforeEach() {
        userController = new UserController(userService);
        user = User.builder()
                .name("Donald")
                .email("Trump@yandex.ru")
                .login("donald123")
                .birthday(
                        LocalDate.of(1946, Month.JUNE, 14))
                .build();
        validExecutor = () -> userService.validate(user);
    }

    @Test
    public void shouldThrowValidateExceptionIfFilmIsNull() {
        User nullUser = null;
        assertThrows(ValidationException.class, () -> userService.validate(nullUser));
    }

    @Test
    public void shouldAssertTrueWhenUserHasAllFieldCorrect() {
        assertDoesNotThrow(() -> userService.validate(user));
    }

    @Test
    public void shouldNotThrowValidateExceptionEvenNameIsNullAndSetNameLikeLogin() {
        user.setName(null);
        assertDoesNotThrow(validExecutor);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void shouldThrowValidateExceptionIfEmailIsNull_IsEmpty_WithoutDog() {
        user.setEmail(null);
        assertThrows(ValidationException.class, validExecutor);
        user.setEmail("    ");
        assertThrows(ValidationException.class, validExecutor);
        user.setEmail("Trump_yandex.ru");
        assertThrows(ValidationException.class, validExecutor);
    }

    @Test
    public void shouldThrowValidateExceptionIfLoginIsNull_IsEmpty() {
        user.setLogin(null);
        assertThrows(ValidationException.class, validExecutor);
        user.setLogin("    ");
        assertThrows(ValidationException.class, validExecutor);
    }

    @Test
    public void shouldThrowValidateExceptionIfBirthdayIsNull_IsAfterNow() {
        user.setBirthday(null);
        assertThrows(ValidationException.class, validExecutor);
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, validExecutor);
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(validExecutor);
    }
}
