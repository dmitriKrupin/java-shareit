package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemMapperTest {
    User user;
    ItemRequest itemRequest;
    Item item;
    ItemDtoIn itemDtoIn;
    ItemDtoOut itemDtoOut;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user@user.com", "userName");
        item = new Item(1L, "itemName", "itemDescription",
                true, user, itemRequest);
        itemDtoIn = new ItemDtoIn(1L, "itemName",
                "itemDescription", true);
        itemDtoOut = new ItemDtoOut(1L, "itemName",
                "itemDescription", true);
    }

    @Test
    void toItemDtoOut() {
        ItemDtoOut itemDtoOutForTest = ItemMapper.toItemDtoOut(item);
        assertNotNull(itemDtoOutForTest);
        assertEquals(itemDtoOut, itemDtoOutForTest);
    }
}