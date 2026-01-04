package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    public static final LocalDate CINEMA_BIRTH = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        // Проверка существования фильма
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с ID " + filmId + " не найден");
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void validate(Film film) {
        if (film == null) {
            log.warn("Получен запрос с null значением фильма");
            throw new ValidationException("Получен запрос с null значением");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не указано");
            throw new ValidationException("Укажите название фильма");
        } else if (film.getDescription() == null) {
            log.warn("Отсутствует описание фильма {}", film.getName());
            throw new ValidationException("Отсутствует описание фильма");
        } else if (film.getDescription().length() > 200) {
            log.warn("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма превышает 200 символов");
        } else if (film.getReleaseDate() == null) {
            log.warn("Отсутствует дата релиза фильма {}", film.getName());
            throw new ValidationException("Отсутствует дата релиза фильма");
        } else if (film.getReleaseDate().isBefore(CINEMA_BIRTH)) {
            log.warn("Дата релиза фильма раньше {}, невозможно", CINEMA_BIRTH);
            throw new ValidationException(String.format("Релиз фильма должен быть после %s", CINEMA_BIRTH));
        } else if (film.getDuration() < 0) {
            log.warn("Указано отрицательное время продолжительности фильма");
            throw new ValidationException("Укажите положительное время продолжительности фильма");
        }
    }
}
