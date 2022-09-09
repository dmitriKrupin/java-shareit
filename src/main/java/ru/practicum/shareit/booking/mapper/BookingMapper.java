package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoOutPatch;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingDtoIn toBookingDto(Booking booking) {
        return new BookingDtoIn(
                booking.getId(),
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingDtoOut toBookingDtoOut(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new ItemDtoOutPatch.BookingDto.UserDto(booking.getBooker().getId()),
                new ItemDtoOutPatch.BookingDto.ItemDto(booking.getItem().getId(), booking.getItem().getName()));
    }

    public static ItemDtoOutPost.BookingDto toBookingDtoForPostOutput(Booking booking) {
        return new ItemDtoOutPost.BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getId());
    }

    public static ItemDtoOutPatch.BookingDto toBookingDtoForPatchOutput(Booking booking) {
        return new ItemDtoOutPatch.BookingDto(
                booking.getId(),
                booking.getStatus(),
                new ItemDtoOutPatch.BookingDto.UserDto(booking.getBooker().getId()),
                new ItemDtoOutPatch.BookingDto.ItemDto(
                        booking.getItem().getId(),
                        booking.getItem().getName())
        );
    }

    public static Booking toBooking(BookingDtoIn bookingDtoIn) {
        return new Booking(
                bookingDtoIn.getId(),
                bookingDtoIn.getStart(),
                bookingDtoIn.getEnd(),
                bookingDtoIn.getItem(),
                bookingDtoIn.getBooker(),
                bookingDtoIn.getStatus()
        );
    }

    public static List<BookingDtoOut> toBookingDtosList(List<Booking> bookingsList) {
        List<BookingDtoOut> bookingDtosListIn = new ArrayList<>();
        for (Booking entry : bookingsList) {
            bookingDtosListIn.add(toBookingDtoOut(entry));
        }
        return bookingDtosListIn;
    }
}
