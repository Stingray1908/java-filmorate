package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    public static final LocalDate CINEMA_BIRTH = LocalDate
            .of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();


    @Override
    public ResponseEntity<Collection<Film>> getAll() {
        return ResponseEntity.ok().body(films.values());
    }

    @Override
    protected void validate(Film film) {
        super.validate(film);
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

    @Override
    protected void setId(Film film) {
        if (film.getId() != 0) {
            return;
        }
        film.setId(count++);
    }

    @Override
    protected int getId(Film film) {
        return film.getId();
    }

    @Override
    protected void putToMap(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    protected boolean containsKey(Film film) {
        return films.containsKey(film.getId());
    }
}
