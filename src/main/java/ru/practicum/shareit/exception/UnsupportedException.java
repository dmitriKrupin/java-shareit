package ru.practicum.shareit.exception;

public class UnsupportedException extends RuntimeException {
    private final String error;

    public UnsupportedException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
