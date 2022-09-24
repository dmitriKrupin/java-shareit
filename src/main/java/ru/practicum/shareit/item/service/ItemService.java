package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoOut addItem(ItemDtoIn itemDtoIn, Long userId);

    ItemDtoOut updateItem(long itemId, ItemDtoIn updateItemDtoIn, Long userId);

    void deleteItemDto(Item itemForDelete);

    ItemDtoOutPost findItemById(long itemId, long userId);

    List<ItemDtoOutPost> findAllItemDtoByUserId(long userId);

    List<ItemDtoOut> getItemsDtoBySearch(String text);

    CommentDtoOut addComment(CommentDtoIn commentDtoIn, long itemId, Long userId);
}
