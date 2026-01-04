package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@ControllerAdvice(assignableTypes = FilmController.class)
@Slf4j
public class FilmExceptionController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleFilmValidationError(ValidationException ex) {
        log.warn("Ошибка валидации фильма: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleFilmNotFound(IllegalArgumentException ex) {
        log.warn("Фильм не найден: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "FILM_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleFilmInternalError(Exception ex) {
        log.error("Внутренняя ошибка при обработке фильма: ", ex);
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "Произошла внутренняя ошибка сервера",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFilmInternalError(NotFoundException ex) {
        log.error("Фильм не найден: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "FILM_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
