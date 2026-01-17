package ru.yandex.practicum.filmorate.exception;

public class DuplicateFriendRequestException extends RuntimeException {
    public DuplicateFriendRequestException(String message) {
        super(message);
    }
}
