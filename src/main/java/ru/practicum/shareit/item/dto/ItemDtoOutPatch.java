package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemDtoOutPatch {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking; //last текущее или последнее завершенное
    private BookingDto nextBooking; //next следующее

    @Data
    @AllArgsConstructor
    public static class BookingDto {
        Long id;
        Status status;
        UserDto booker;
        ItemDto item;

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
}
