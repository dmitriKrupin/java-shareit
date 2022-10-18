package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoOut addItem(ItemDtoIn itemDtoIn, Long userId);

    ItemDtoOut updateItem(long itemId, ItemDtoIn updateItemDtoIn, Long userId);

    void deleteItemDto(Item itemForDelete);

    ItemDtoOutPost findItemById(long itemId, long userId);

    List<ItemDtoOutPost> findAllItemDtoByUserId(long userId, PageRequest pageRequest);

    List<ItemDtoOut> getItemsDtoBySearch(String text, PageRequest pageRequest);

    CommentDtoOut addComment(CommentDtoIn commentDtoIn, Long itemId, Long userId);
}
