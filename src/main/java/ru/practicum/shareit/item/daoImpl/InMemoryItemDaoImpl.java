package ru.practicum.shareit.item.daoImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;

@Slf4j
@Component
public class InMemoryItemDaoImpl implements ItemDao {
    private final HashMap<Long, ArrayList<Item>> itemsMap = new HashMap<>();
    private final ExceptionController exceptionController;
    private final UserService userService;

    private long itemIdCounter = 0;

    public InMemoryItemDaoImpl(ExceptionController exceptionController, UserService userService) {
        this.exceptionController = exceptionController;
        this.userService = userService;
    }

    @Override
    public Item addItem(Item item, String userId) {
        if (isValidateNewItem(item) && isVlidateSharerUser(userId, false)) {
            itemIdCounter++;
            item.setId(itemIdCounter);
            ArrayList<Item> itemList = new ArrayList<>();
            itemList.add(item);
            itemsMap.put(Long.parseLong(userId), itemList);
        }
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item updateItem, String userId) {
        Item oldItem = new Item();
        if (isVlidateSharerUser(userId, true)) {
            oldItem = findItemById(itemId);
            if (updateItem.getName() != null) {
                oldItem.setName(updateItem.getName());
            }
            if (updateItem.getAvailable() != null) {
                oldItem.setAvailable(updateItem.getAvailable());
            }
            if (updateItem.getDescription() != null) {
                oldItem.setDescription(updateItem.getDescription());
            }
        }
        return oldItem;
    }

    @Override
    public void deleteItem(Item itemForDelete) {
        itemsMap.remove(itemForDelete.getId());
    }

    @Override
    public Item findItemById(long itemId) {
        Item item = new Item();
        for (Map.Entry<Long, ArrayList<Item>> entry : itemsMap.entrySet()) {
            for (Item itemEntry : entry.getValue()) {
                if (itemId == itemEntry.getId()) {
                    item = itemEntry;
                }
            }
        }
        return item;
    }

    @Override
    public List<Item> findAllItemByUserId(long userId) {
        ArrayList<Item> itemList = new ArrayList<>();
        if (itemsMap.containsKey(userId)) {
            itemList = itemsMap.get(userId);
        }
        return itemList;
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        ArrayList<Item> itemList = new ArrayList<>();
        String search = text.toLowerCase(Locale.ROOT);
        if (text.isEmpty()) {
            return itemList;
        } else {
            for (Map.Entry<Long, ArrayList<Item>> entry : itemsMap.entrySet()) {
                for (Item itemEntry : entry.getValue()) {
                    if (itemEntry.getName().toLowerCase(Locale.ROOT).contains(search) ||
                            itemEntry.getDescription().toLowerCase(Locale.ROOT).contains(search) &&
                                    itemEntry.getAvailable()) {
                        itemList.add(itemEntry);
                    }
                }
            }
        }
        return itemList;
    }

    private boolean isValidateNewItem(Item item) {
        boolean isValidateNewItem = true;
        if (item.getName().isEmpty()) {
            log.error("Пустое имя пользователя.");
            isValidateNewItem = false;
            throw exceptionController.badRequestException(new BadRequestException("Имя не может быть пустым."));
        }
        if (item.getAvailable() == null) {
            log.error("Поле доступности вещи отсутствует.");
            isValidateNewItem = false;
            throw exceptionController.badRequestException(new BadRequestException("Поле доступности не может быть пустым."));
        }
        if (item.getDescription() == null) {
            log.error("Поле описания вещи отсутствует.");
            isValidateNewItem = false;
            throw exceptionController.badRequestException(new BadRequestException("Поле описания не может быть пустым."));
        }
        return isValidateNewItem;
    }

    private boolean isVlidateSharerUser(String userId, boolean isUpdate) {
        boolean isVlidateSharerUser = true;
        if (userId == null) {
            log.error("Пустое поле X-Sharer-User-Id.");
            isVlidateSharerUser = false;
            throw exceptionController.badRequestException(new BadRequestException("Пустое поле X-Sharer-User-Id."));
        } else {
            User user = userService.findUserById(Long.parseLong(userId));
            List<Item> itemList = findAllItemByUserId(Long.parseLong(userId));
            if (isUpdate) {
                if (itemsMap.containsValue(itemList) &&
                        itemsMap.containsKey(Long.parseLong(userId)) &&
                        userService.getAllUsersList().contains(user)) {
                    isVlidateSharerUser = true;
                    return isVlidateSharerUser;
                } else {
                    log.error("Нет пользователя с таким id {}.", Long.parseLong(userId));
                    isVlidateSharerUser = false;
                    throw exceptionController.notFoundException(new NotFoundException("Такого пользователя не существует."));
                }
            } else {
                if (userService.getAllUsersList().contains(user)) {
                    isVlidateSharerUser = true;
                    return isVlidateSharerUser;
                } else {
                    log.error("Нет пользователя с таким id {}.", Long.parseLong(userId));
                    isVlidateSharerUser = false;
                    throw exceptionController.notFoundException(new NotFoundException("Такого пользователя не существует."));
                }
            }
        }
    }
}
