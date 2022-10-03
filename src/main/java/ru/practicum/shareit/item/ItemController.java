package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/items")
@Validated
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemDtoOut addItem(
            @Validated({Create.class})
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /items");
        return itemService.addItem(itemDtoIn, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(
            @PathVariable long itemId,
            @Validated @RequestBody CommentDtoIn commentDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /{}/comment", itemId);
        return itemService.addComment(commentDtoIn, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(
            @Validated({Update.class})
            @PathVariable long itemId,
            @RequestBody ItemDtoIn updateItemDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем PATCH запрос к эндпойнту /items");
        return itemService.updateItem(itemId, updateItemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOutPost getItemById(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /items/{}", itemId);
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOutPost> getAllItemByUserId(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /items");
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemService.findAllItemDtoByUserId(userId, pageRequest);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getItemsBySearch(
            @RequestParam String text,
            @PositiveOrZero
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @Positive
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /items/search?text={}", text);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemService.getItemsDtoBySearch(text, pageRequest);
    }
}