package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item addItem(Item item, String userId);

    Item updateItem(long itemId, Item updateItem, String userId);

    void deleteItem(Item itemForDelete);

    Item findItemById(long itemId);

    List<Item> findAllItemByUserId(long userId);

    List<Item> getItemsBySearch(String text);
}
