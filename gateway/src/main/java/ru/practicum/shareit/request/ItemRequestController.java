package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @Validated @RequestBody ItemRequestDtoIn itemRequestDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /requests");
        return itemRequestClient.addRequest(itemRequestDtoIn, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByUserId(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            @Validated @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /requests");
        return itemRequestClient.getAllRequestsByUserId(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsOtherUsers(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /requests/all?from={}&size={}", from, size);
        return itemRequestClient.getAllRequestsOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @PathVariable long requestId,
            @Validated @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /requests/{}", requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

}