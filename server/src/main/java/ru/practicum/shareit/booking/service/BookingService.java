package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut addBooking(BookingDtoIn bookingDtoIn, Long userId);

    BookingDtoOut approvedBooking(Long userId, Long bookingId, Boolean approved);

    List<BookingDtoOut> getAllBookingsByBookerId(
            Long bookerId, String state, PageRequest pageRequest);

    List<BookingDtoOut> getAllBookingsByOwnerId(
            Long ownerId, String state, PageRequest pageRequest);

    BookingDtoOut getBookingById(Long bookingId, Long userId);
}
