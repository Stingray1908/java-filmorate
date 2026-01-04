package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(exclude = "friendIds")
public class User {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    final Set<Integer> friendIds = new HashSet<>();

    public void addFriend(int friendId) {
        friendIds.add(friendId);
    }

    public void removeFriend(int friendId) {
        friendIds.remove(friendId);
    }
}
