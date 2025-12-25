package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.controller.FilmController.CINEMA_BIRTH;

public class FilmControllerTest {

    private FilmController filmController;
    private Film film;
    private Executable validExecutor;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = Film.builder()
                .name("Sleepy Hollow")
                .description("Horror")
                .duration(50)
                .releaseDate(LocalDate.of(1999, Month.NOVEMBER, 17))
                .build();
        validExecutor = () -> filmController.validate(film);
    }

    @Test
    public void shouldThrowValidateExceptionIfFilmIsNull() {
        Film nullFilm = null;
        assertThrows(ValidationException.class, () -> filmController.validate(nullFilm));
    }

    @Test
    public void shouldAssertTrueWhenUserHasAllFieldCorrect() {
        assertDoesNotThrow(() -> filmController.validate(film));
    }

    @Test
    public void shouldThrowValidateExceptionIfNameIsNull_IsEmpty() {
        film.setName(null);
        assertThrows(ValidationException.class, validExecutor);
        film.setName("   ");
        assertThrows(ValidationException.class, validExecutor);
    }

    @Test
    public void shouldThrowValidateExceptionIfDescriptionLengthMore200Symbols() {
        film.setDescription("s".repeat(199));
        assertDoesNotThrow(validExecutor);
        film.setDescription("s".repeat(200));
        assertDoesNotThrow(validExecutor);
        film.setDescription("s".repeat(201));
        assertThrows(ValidationException.class, validExecutor);
    }

    @Test
    public void shouldThrowValidateExceptionIfDateOfReleaseIsNull_IsBeforeCINEMA_BIRTH() {
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
    public void shouldThrowValidateExceptionIfDurationIsNull_IsNegativeDigit() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, validExecutor);
        film.setDuration(0);
        assertDoesNotThrow(validExecutor);
        film.setDuration(1);
        assertDoesNotThrow(validExecutor);
    }
}
