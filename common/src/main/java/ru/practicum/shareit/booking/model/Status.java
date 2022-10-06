package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;

    public static Optional<Status> from(String stringState) {
        for (Status state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
