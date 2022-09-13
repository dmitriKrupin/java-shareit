package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;

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
                new BookingDtoOut.UserDto(booking.getBooker().getId()),
                new BookingDtoOut.ItemDto(booking.getItem().getId(), booking.getItem().getName()));
    }

    public static BookingDtoOut toBookingDtoForPostOutput(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getId());
    }

    public static BookingDtoOut toBookingDtoForPatchOutput(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStatus(),
                new BookingDtoOut.UserDto(booking.getBooker().getId()),
                new BookingDtoOut.ItemDto(
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
