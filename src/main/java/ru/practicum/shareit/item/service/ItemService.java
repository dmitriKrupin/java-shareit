package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {
    Item addItem(Item item, String userId);

    Item updateItem(long itemId, Item updateItem, String userId);

    void deleteItem(Item itemForDelete);

    Item findItemById(long itemId);

    List<Item> findAllItemByUserId(long userId);

    List<Item> getItemsBySearch(String text);
}
