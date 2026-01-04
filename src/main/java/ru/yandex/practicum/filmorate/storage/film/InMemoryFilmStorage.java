package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int count = 1;

    @Override
    public Film create(Film film) {
        if (film.getId() != null) {
            throw new IllegalArgumentException("Нельзя создать фильм с уже заданным ID");
        }
        film.setId(count++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public void addLike(int filmId, int userId) { // было: long userId
        Film film = getById(filmId);
        if (film == null) {
            String s = "Фильм с id " + filmId + " не найден";
            log.debug(s);
            throw new IllegalArgumentException(s);
        }
        film.addLike(userId);
        update(film);
    }

    @Override
    public void removeLike(int filmId, int userId) { // было: long userId
        Film film = getById(filmId);
        if (film == null) {
            throw new IllegalArgumentException("Фильм с id " + filmId + " не найден");
        }
        if (!film.getLikes().contains(userId)) {
            String s = "Пользователь с ID:" + userId + " не ставил like этому фильму";
            log.debug(s);
            throw new NotFoundException(s);
        }
        film.removeLike(userId);
        update(film);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikesCount(), f1.getLikesCount()))
                .limit(count)
                .collect(java.util.stream.Collectors.toList());
    }
}
