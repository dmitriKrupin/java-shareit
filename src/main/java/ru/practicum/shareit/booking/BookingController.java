package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingDtoOut addBooking(
            @RequestBody BookingDtoIn bookingDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /items");
        return bookingService.addBooking(bookingDtoIn, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDtoOut approvedBooking(
            @PathVariable long bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем PATCH запрос к эндпойнту /{}?approved={}", bookingId, approved);
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingDtoOut> getAllBookingsByUser(
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestParam(required = false, name = "state") String state,
            @PositiveOrZero
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /bookings?state={}", state);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return bookingService.getAllBookingsByBookerId(bookerId, state, pageRequest);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoOut> getBookingsForAllItemsByUser(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(required = false, name = "state") String state,
            @PositiveOrZero
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /owner");
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return bookingService.getAllBookingsByOwnerId(ownerId, state, pageRequest);
    }

    @GetMapping(value = "{bookingId}")
    public BookingDtoOut getBookingById(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /{}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }
}
