package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutPatch;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;

import java.util.List;

@Service
public interface BookingService {
    ItemDtoOutPost.BookingDto addBooking(BookingDtoIn bookingDtoIn, String userId);

    ItemDtoOutPatch.BookingDto approvedBooking(String userId, Long bookingId, Boolean approved);

    List<BookingDtoOut> getAllBookingsByBookerId(String bookerId, String state);

    List<BookingDtoOut> getAllBookingsByOwnerId(String ownerId, String state);

    BookingDtoOut getBookingById(long bookingId, String userId);
}
