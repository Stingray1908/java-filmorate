package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Builder
public class Friendship {
    @Getter
    private final int user;
    @Getter
    private final int friend;

    public Friendship(int user, int friend) {
        this.user = user;
        this.friend = friend;
    }
}
