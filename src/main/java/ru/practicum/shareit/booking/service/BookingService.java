package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut addBooking(BookingDtoIn bookingDtoIn, String userId);

    BookingDtoOut approvedBooking(String userId, Long bookingId, Boolean approved);

    List<BookingDtoOut> getAllBookingsByBookerId(
            String bookerId, String state, PageRequest pageRequest);

    List<BookingDtoOut> getAllBookingsByOwnerId(
            String ownerId, String state, PageRequest pageRequest);

    BookingDtoOut getBookingById(long bookingId, String userId);
}
