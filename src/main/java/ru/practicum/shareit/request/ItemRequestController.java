package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    @Autowired
    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut addRequest(
            @Validated @RequestBody ItemRequestDtoIn itemRequestDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /requests");
        return itemRequestService.addRequest(itemRequestDtoIn, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getAllRequestsByUserId(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            @Validated @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /requests");
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemRequestService.getAllRequestsByUserId(userId, pageRequest);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAllRequestsOtherUsers(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /requests/all?from={}&size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemRequestService.getAllRequestsOtherUsers(userId, pageRequest);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getRequestById(
            @PathVariable long requestId,
            @Validated @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /requests/{}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }

}