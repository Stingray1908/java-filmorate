package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.service.FilmService.CINEMA_BIRTH;

public class FilmControllerTest {

    private FilmController filmController;
    private Film film;
    private Executable validExecutor;
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();
    private final FilmService filmService = new FilmService(filmStorage, new UserService(userStorage));

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(filmService);
        film = Film.builder()
                .name("Sleepy Hollow")
                .description("Horror")
                .duration(50)
                .releaseDate(LocalDate.of(1999, Month.NOVEMBER, 17))
                .build();
        validExecutor = () -> filmService.validate(film);
    }

    @Test
    void shouldThrowValidateExceptionIfFilmIsNull() {
        Film nullFilm = null;
        assertThrows(ValidationException.class, () -> filmService.validate(nullFilm));
    }

    @Test
    void shouldAssertTrueWhenFilmHasAllFieldsCorrect() {
        assertDoesNotThrow(validExecutor);
    }

    @Test
    void shouldThrowValidateExceptionIfNameIsNullOrEmpty() {
        film.setName(null);
        assertThrows(ValidationException.class, validExecutor);
        film.setName("   ");
        assertThrows(ValidationException.class, validExecutor);
    }

    @Test
    void shouldThrowValidateExceptionIfDescriptionLengthMore200Symbols() {
        film.setDescription("s".repeat(199));
        assertDoesNotThrow(validExecutor);
        film.setDescription("s".repeat(200));
        assertDoesNotThrow(validExecutor);
        film.setDescription("s".repeat(201));
        assertThrows(ValidationException.class, validExecutor);
    }

    @Test
    void shouldThrowValidateExceptionIfDateOfReleaseIsNullOrBeforeCINEMA_BIRTH() {
        film.setReleaseDate(null);
        assertThrows(ValidationException.class, validExecutor);
        film.setReleaseDate(CINEMA_BIRTH.minusDays(1));
        assertThrows(ValidationException.class, validExecutor);
        film.setReleaseDate(CINEMA_BIRTH);
        assertDoesNotThrow(validExecutor);
        film.setReleaseDate(CINEMA_BIRTH.plusDays(1));
        assertDoesNotThrow(validExecutor);
    }

    @Test
    void shouldThrowValidateExceptionIfDurationIsNegative() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, validExecutor);
        film.setDuration(0);
        assertDoesNotThrow(validExecutor);
        film.setDuration(1);
        assertDoesNotThrow(validExecutor);
    }
}
