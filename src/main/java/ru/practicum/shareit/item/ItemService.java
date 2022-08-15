package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface ItemService {
    ItemDto addItem(ItemDto itemDto, String userId);

    ItemDto updateItem(long itemId, ItemDto updateItemDto, String userId);

    void deleteItem();

    ItemDto findItemDtoById(long itemId);

    List<ItemDto> findAllItemByUserId(long parseLong);

    List<ItemDto> getItemsBySearch(String text);
}
