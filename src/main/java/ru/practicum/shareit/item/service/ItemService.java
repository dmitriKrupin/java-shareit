package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoIn addItem(ItemDtoIn itemDtoIn, String userId);

    ItemDtoIn updateItem(long itemId, ItemDtoIn updateItemDtoIn, String userId);

    void deleteItemDto(Item itemForDelete);

    ItemDtoOutPost findItemById(long itemId, String userId);

    List<ItemDtoOutPost> findAllItemDtoByUserId(long userId);

    List<ItemDtoIn> getItemsDtoBySearch(String text);

    CommentDtoOut addComment(CommentDtoIn commentDtoIn, long itemId, String userId);
}
