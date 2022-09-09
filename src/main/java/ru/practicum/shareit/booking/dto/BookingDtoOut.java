package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoOutPatch;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoOut {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    ItemDtoOutPatch.BookingDto.UserDto booker;
    ItemDtoOutPatch.BookingDto.ItemDto item;
}