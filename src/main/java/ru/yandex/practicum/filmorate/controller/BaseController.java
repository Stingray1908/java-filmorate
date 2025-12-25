package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;

public abstract class
BaseController<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected int count = 1;

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T t) {
        try {
            validate(t);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(t);
        }
        setId(t);
        putToMap(t);
        log.info("Значение класса: \"{}\", id:{} добавлено в таблицу", t.getClass().getSimpleName(), getId(t));
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @PutMapping
    public ResponseEntity<T> update(@RequestBody T t) {
        log.info("Исходный объект перед валидацией: {}", t);
        validate(t);
        try {
            validate(t);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(t);
        }
        int id = getId(t);
        if (!containsKey(t)) {
            log.warn("Значения c id:{} не нейдено", id);
            //throw new NotFoundException(String.format("Данные с id:%d отсутствуют", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(t);
        }
        putToMap(t);
        log.info("Значение класса: \"{}\", id:{} обновлено в таблице", t.getClass().getSimpleName(), getId(t));
        return ResponseEntity.ok().body(t);
    }

    @GetMapping
    public abstract ResponseEntity<Collection<T>> getAll();

    protected void validate(T t) {
        if (t == null) {
            log.warn("{} получил null элемент", getClass().getSimpleName());
            throw new ValidationException("Получен запрос с null значением");
        }
    }

    protected abstract void setId(T t);

    protected abstract int getId(T t);

    protected abstract void putToMap(T t);

    protected abstract boolean containsKey(T t);
}
