package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoOutPatch;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ItemDtoOutPost.BookingDto addBooking(
            @RequestBody BookingDtoIn bookingDtoIn,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем POST запрос к эндпойнту /items");
        return bookingService.addBooking(bookingDtoIn, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public ItemDtoOutPatch.BookingDto approvedBooking(
            @PathVariable long bookingId,
            @RequestParam boolean approved,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем PATCH запрос к эндпойнту /{}?approved={}", bookingId, approved);
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingDtoOut> getAllBookingsByUser(
            @RequestHeader HttpHeaders header,
            @RequestParam(required = false, name = "state") String state) {
        String bookerId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /bookings?state={}", state);
        return bookingService.getAllBookingsByBookerId(bookerId, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoOut> getBookingsForAllItemsByUser(
            @RequestHeader HttpHeaders header,
            @RequestParam(required = false, name = "state") String state) {
        String ownerId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /owner");
        return bookingService.getAllBookingsByOwnerId(ownerId, state);
    }

    @GetMapping(value = "{bookingId}")
    public BookingDtoOut getBookingById(
            @PathVariable long bookingId,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /{}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }
}
