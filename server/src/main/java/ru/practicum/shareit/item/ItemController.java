package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemDtoOut addItem(
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем POST запрос к эндпойнту /items");
        return itemService.addItem(itemDtoIn, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(
            @PathVariable long itemId,
            @RequestBody CommentDtoIn commentDtoIn,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем GET запрос к эндпойнту /{}/comment", itemId);
        return itemService.addComment(commentDtoIn, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(
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
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /items");
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemService.findAllItemDtoByUserId(userId, pageRequest);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getItemsBySearch(
            @RequestParam String text,
            @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получаем GET запрос к эндпойнту /items/search?text={}", text);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemService.getItemsDtoBySearch(text, pageRequest);
    }
}