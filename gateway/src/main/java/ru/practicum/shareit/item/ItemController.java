package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@Slf4j
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(
            @Validated({Create.class})
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /items");
        return itemClient.addItem(itemDtoIn, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @PathVariable long itemId,
            @Validated @RequestBody CommentDtoIn commentDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /{}/comment", itemId);
        return itemClient.addComment(commentDtoIn, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @Validated({Update.class})
            @PathVariable long itemId,
            @RequestBody ItemDtoIn updateItemDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем PATCH запрос к эндпойнту /items");
        return itemClient.updateItem(itemId, updateItemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /items/{}", itemId);
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemByUserId(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /items");
        return itemClient.findAllItemDtoByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(
            @RequestParam String text,
            @PositiveOrZero
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /items/search?text={}", text);
        return itemClient.getItemsDtoBySearch(text, from, size);
    }
}