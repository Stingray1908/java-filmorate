package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(exclude = "friendIds")
public class User {
    Integer id;
    String name;
    String email;
    String login;
    LocalDate birthday;
    final Set<Integer> friendIds = new HashSet<>();
    final Set<Integer> sentRequests = new LinkedHashSet<>();
    final Set<Integer> receivedRequests = new LinkedHashSet<>();

    public void addFriend(int friendId) {
        friendIds.add(friendId);
        sentRequests.remove(friendId);
        receivedRequests.remove(friendId);
    }

    public void sendRequests(int friendId) {
        sentRequests.add(friendId);
    }

    public void receiveRequests(int friendId) {
        receivedRequests.add(friendId);
    }

    public void removeFriend(int friendId) {
        friendIds.remove(friendId);
    }
}
