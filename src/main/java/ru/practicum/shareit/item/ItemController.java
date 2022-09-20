package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
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
            @Validated({Create.class})
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем POST запрос к эндпойнту /items");
        return itemService.addItem(itemDtoIn, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(
            @PathVariable long itemId,
            @Validated @RequestBody CommentDtoIn commentDtoIn,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /{}/comment", itemId);
        return itemService.addComment(commentDtoIn, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(
            @Validated({Update.class})
            @PathVariable long itemId,
            @RequestBody ItemDtoIn updateItemDtoIn,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем PATCH запрос к эндпойнту /items");
        return itemService.updateItem(itemId, updateItemDtoIn, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOutPost getItemById(@PathVariable long itemId,
                                      @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /items/{}", itemId);
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOutPost> getAllItemByUserId(@RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /items");
        assert userId != null;
        return itemService.findAllItemDtoByUserId(Long.parseLong(userId));
    }

    @GetMapping("/search")
    public List<ItemDtoOut> getItemsBySearch(@RequestParam String text) {
        log.info("Получаем GET запрос к эндпойнту /items/search?text={}", text);
        return itemService.getItemsDtoBySearch(text);
    }
}