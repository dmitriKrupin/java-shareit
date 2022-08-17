package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Для каждого из данных сценариев создайте соответственный метод в контроллере. Также создайте интерфейс ItemService и
 * реализующий его класс ItemServiceImpl, к которому будет обращаться ваш контроллер. В качестве DAO создайте реализации,
 * которые будут хранить данные в памяти приложения. Работу с базой данных вы реализуете в следующем спринте.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    //Добавление новой вещи. Будет происходить по эндпойнту POST /items. На вход поступает объект ItemDto.
    // userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь.
    // Именно этот пользователь — владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов,
    // рассмотренных далее.
    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем POST запрос к эндпойнту /items");
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemService.addItem(item, userId));
    }

    //Редактирование вещи. Эндпойнт PATCH /items/{itemId}. Изменить можно название, описание и статус доступа к аренде.
    // Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable long itemId,
            @RequestBody ItemDto updateItemDto,
            @RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем PATCH запрос к эндпойнту /items");
        Item updateItem = ItemMapper.toItem(updateItemDto);
        return ItemMapper.toItemDto(itemService.updateItem(itemId, updateItem, userId));
    }

    //Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}.
    // Информацию о вещи может просмотреть любой пользователь.
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        log.info("Получаем GET запрос к эндпойнту /items/{}", itemId);
        return ItemMapper.toItemDto(itemService.findItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAllItemByUserId(@RequestHeader HttpHeaders header) {
        String userId = header.getFirst("X-Sharer-User-Id");
        log.info("Получаем GET запрос к эндпойнту /items");
        assert userId != null;
        return ItemMapper.toItemsListDto(itemService.findAllItemByUserId(Long.parseLong(userId)));
    }

    //Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи,
    // содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text},
    // в text передаётся текст для поиска. Проверьте, что поиск возвращает только доступные для аренды вещи.
    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        log.info("Получаем GET запрос к эндпойнту /items/search?text={}", text);
        return ItemMapper.toItemsListDto(itemService.getItemsBySearch(text));
    }
}