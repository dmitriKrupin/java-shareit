package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemRequestMapperTest {

    @Test
    void toItemRequestDtoOutList() {
        ItemRequest itemRequest = new ItemRequest(1L, "description",
                new User(1L, "user@user.com", "user"),
                LocalDateTime.of(2022, 9, 28,
                        4, 12, 16));
        ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut(1L,
                "description", LocalDateTime.of(2022, 9, 28,
                4, 12, 16));

        List<ItemRequest> itemRequestsList = List.of(itemRequest);
        List<ItemRequestDtoOut> itemRequestDtoOutList = ItemRequestMapper
                .toItemRequestDtoOutList(itemRequestsList);
        assertNotNull(itemRequestDtoOutList);
        assertEquals(itemRequestDtoOut, itemRequestDtoOutList.get(0));
    }
}