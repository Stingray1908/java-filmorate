package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> create(@RequestBody Film film) {
        log.info("Получение запроса на создание фильма: {}", film);
        Film createdFilm = filmService.createFilm(film);
        log.info("Фильм с id:{} успешно создан и добавлен в таблицу", createdFilm.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {
        log.info("Получение запроса на обновление фильма: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Фильм с id:{} успешно обновлён в таблице", updatedFilm.getId());
        return ResponseEntity.ok().body(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAll() {
        log.info("Получение запроса на получение списка всех фильмов");
        Collection<Film> films = filmService.getAllFilms();
        log.info("Получено {} фильмов из сервиса", films.size());
        return ResponseEntity.ok(films);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(
            @PathVariable int id,
            @PathVariable int userId) { // было: long userId
        log.info("Получение запроса на добавление лайка: filmId={}, userId={}", id, userId);
        filmService.addLike(id, userId);
        log.info("Лайк успешно добавлен для фильма с id:{} от пользователя с id:{}", id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(
            @PathVariable int id,
            @PathVariable int userId) { // было: long userId
        log.info("Получение запроса на удаление лайка: filmId={}, userId={}", id, userId);
        filmService.removeLike(id, userId);
        log.info("Лайк успешно удалён для фильма с id:{} от пользователя с id:{}", id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopular(
            @RequestParam(defaultValue = "10") int count) {
        log.info("Получение запроса на популярные фильмы с count={}", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Получено {} популярных фильмов", popularFilms.size());
        return ResponseEntity.ok(popularFilms);
    }
}
