package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.UnsupportedException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(
            @RequestBody BookingDtoIn bookingDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /bookings");
        return bookingClient.addBooking(bookingDtoIn, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> approvedBooking(
            @PathVariable long bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем PATCH запрос к эндпойнту /{}?approved={}", bookingId, approved);
        return bookingClient.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        Status state = Status.from(stateParam)
                .orElseThrow(() -> new UnsupportedException("Unknown state: " + stateParam));
        log.info("Получаем GET запрос к эндпойнту /bookings?state={}", state);
        return bookingClient.getAllBookingsByBookerId(bookerId, state, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getBookingsForAllItemsByUser(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        Status state = Status.from(stateParam)
                .orElseThrow(() -> new UnsupportedException("Unknown state: " + stateParam));
        log.info("Получаем GET запрос к эндпойнту /owner");
        return bookingClient.getAllBookingsByOwnerId(ownerId, state, from, size);
    }

    @GetMapping(value = "{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /{}", bookingId);
        return bookingClient.getBookingById(userId, bookingId);
    }
}
