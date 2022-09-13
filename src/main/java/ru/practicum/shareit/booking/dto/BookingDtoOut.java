package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
public class BookingDtoOut {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId;
    private Status status;
    private UserDto booker;
    private ItemDto item;

    public BookingDtoOut(Long id, LocalDateTime start, LocalDateTime end, Status status, UserDto booker, ItemDto item) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }

    public BookingDtoOut(Long id, Status status, UserDto booker, ItemDto item) {
        this.id = id;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }

    public BookingDtoOut(Long id, LocalDateTime start, LocalDateTime end, Long bookerId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.bookerId = bookerId;
    }

    @Data
    @AllArgsConstructor
    public static class UserDto {
        Long id;
    }

    @Data
    @AllArgsConstructor
    public static class ItemDto {
        Long id;
        String name;
    }
}
