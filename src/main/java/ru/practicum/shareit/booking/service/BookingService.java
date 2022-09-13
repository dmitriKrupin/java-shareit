package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut addBooking(BookingDtoIn bookingDtoIn, String userId);

    BookingDtoOut approvedBooking(String userId, Long bookingId, Boolean approved);

    List<BookingDtoOut> getAllBookingsByBookerId(String bookerId, String state);

    List<BookingDtoOut> getAllBookingsByOwnerId(String ownerId, String state);

    BookingDtoOut getBookingById(long bookingId, String userId);
}
