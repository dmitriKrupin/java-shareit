package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    void toItemsListDtoOutPost() {
        List<Item> itemsList = List.of(item);
        CommentDtoOut comments = new CommentDtoOut(1L, "text of comment",
                "author of comment", LocalDateTime.now().plusHours(4));
        List<ItemDtoOutPost> itemDtoOutPostListForTest = ItemMapper
                .toItemsListDtoOutPost(
                        itemsList,
                        new Booking(1L, LocalDateTime.now().plusMinutes(10),
                                LocalDateTime.now().plusHours(1),
                                item, user, Status.WAITING),
                        new Booking(2L, LocalDateTime.now().plusHours(2),
                                LocalDateTime.now().plusHours(3),
                                item, user, Status.WAITING),
                        List.of(comments));
        assertNotNull(itemDtoOutPostListForTest);
        assertEquals(comments.getText(),
                itemDtoOutPostListForTest.get(0).getComments().get(0).getText());

    }
}