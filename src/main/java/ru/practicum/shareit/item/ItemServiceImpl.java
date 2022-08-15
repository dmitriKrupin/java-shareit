package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Component
public class ItemServiceImpl implements ItemService {
    private final HashMap<Long, ArrayList<ItemDto>> itemDtoMap = new HashMap<>();
    private final ExceptionController exceptionController;
    private final UserService userService;

    private long itemIdCounter = 0;

    public ItemServiceImpl(ExceptionController exceptionController, UserService userService) {
        this.exceptionController = exceptionController;
        this.userService = userService;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, String userId) {
        if (isValidateNewItem(itemDto) && isVlidateSharerUser(itemDto.getId(), userId, false)) {
            itemIdCounter++;
            itemDto.setId(itemIdCounter);
            ArrayList<ItemDto> itemDtoList = new ArrayList<>();
            itemDtoList.add(itemDto);
            itemDtoMap.put(Long.parseLong(userId), itemDtoList);
        }
        return itemDto;
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto updateItemDto, String userId) {
        ItemDto oldItemDto = new ItemDto();
        if (isVlidateSharerUser(itemId, userId, true)) {
            oldItemDto = findItemDtoById(itemId);
            if (updateItemDto.getName() != null) {
                oldItemDto.setName(updateItemDto.getName());
            }
            if (updateItemDto.getAvailable() != null) {
                oldItemDto.setAvailable(updateItemDto.getAvailable());
            }
            if (updateItemDto.getDescription() != null) {
                oldItemDto.setDescription(updateItemDto.getDescription());
            }
        }
        return oldItemDto;
    }

    @Override
    public void deleteItem() {

    }

    @Override
    public ItemDto findItemDtoById(long itemId) {
        ItemDto itemDto = new ItemDto();
        for (Map.Entry<Long, ArrayList<ItemDto>> entry : itemDtoMap.entrySet()) {
            for (ItemDto itemDtoEntry : entry.getValue()) {
                if (itemId == itemDtoEntry.getId()) {
                    itemDto = itemDtoEntry;
                }
            }
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> findAllItemByUserId(long userId) {
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();
        if (itemDtoMap.containsKey(userId)) {
            itemDtoList = itemDtoMap.get(userId);
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();
        String search = text.toLowerCase(Locale.ROOT);
        if (text.isEmpty()) {
            return itemDtoList;
        } else {
            for (Map.Entry<Long, ArrayList<ItemDto>> entry : itemDtoMap.entrySet()) {
                for (ItemDto itemDtoEntry : entry.getValue()) {
                    if (itemDtoEntry.getName().toLowerCase(Locale.ROOT).contains(search) ||
                            itemDtoEntry.getDescription().toLowerCase(Locale.ROOT).contains(search) &&
                                    itemDtoEntry.getAvailable()) {
                        itemDtoList.add(itemDtoEntry);
                    }
                }
            }
        }
        return itemDtoList;
    }

    private boolean isValidateNewItem(ItemDto itemDto) {
        boolean isValidateNewItem = true;
        if (itemDto.getName().isEmpty()) {
            log.error("Пустое имя пользователя.");
            isValidateNewItem = false;
            throw exceptionController.badRequestException(new BadRequestException("Имя не может быть пустым."));
        }
        if (itemDto.getAvailable() == null) {
            log.error("Поле доступности вещи отсутствует.");
            isValidateNewItem = false;
            throw exceptionController.badRequestException(new BadRequestException("Поле доступности не может быть пустым."));
        }
        if (itemDto.getDescription() == null) {
            log.error("Поле описания вещи отсутствует.");
            isValidateNewItem = false;
            throw exceptionController.badRequestException(new BadRequestException("Поле описания не может быть пустым."));
        }
        return isValidateNewItem;
    }

    private boolean isVlidateSharerUser(long itemId, String userId, boolean isUpdate) {
        User user = userService.findUserById(Long.parseLong(userId));
        List<ItemDto> itemDtoList = findAllItemByUserId(Long.parseLong(userId));
        boolean isVlidateSharerUser = true;
        if (userId == null) {
            log.error("Пустое поле X-Sharer-User-Id.");
            isVlidateSharerUser = false;
            throw exceptionController.badRequestException(new BadRequestException("Пустое поле X-Sharer-User-Id."));
        } else {
            if (isUpdate) {
                if (itemDtoMap.containsValue(itemDtoList) &&
                        itemDtoMap.containsKey(Long.parseLong(userId)) &&
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
