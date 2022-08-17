package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Slf4j
@Component
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;

    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public Item addItem(Item item, String userId) {
        return itemDao.addItem(item, userId);
    }

    @Override
    public Item updateItem(long itemId, Item updateItem, String userId) {
        return itemDao.updateItem(itemId, updateItem, userId);
    }

    @Override
    public void deleteItem(Item itemForDelete) {
        itemDao.deleteItem(itemForDelete);
    }

    @Override
    public Item findItemById(long itemId) {
        return itemDao.findItemById(itemId);
    }

    @Override
    public List<Item> findAllItemByUserId(long userId) {
        return itemDao.findAllItemByUserId(userId);
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        return itemDao.getItemsBySearch(text);
    }
}
