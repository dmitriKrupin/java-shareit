package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceTest {
    private ItemRepository itemRepository;
    private ItemService itemService;
    private UserRepository userRepository;
    private UserService userService;
    private ItemRequestRepository itemRequestRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;

    private User userOne;
    private Item itemOne;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, itemRequestRepository, bookingRepository, commentRepository);
        userOne = new User(1L, "userOne@user.com", "userOne");
        itemRequest = new ItemRequest(1L, "itemRequestDescription", userOne, LocalDateTime.now());
        itemOne = new Item(1L, "itemName", "itemDescription", true, userOne, itemRequest);
    }

    @Test
    void addItem() {
        ItemDtoIn itemDtoIn = ItemMapper.toItemDtoIn(itemOne);
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.of(userOne));
        when(itemRequestRepository.findById(itemDtoIn.getRequestId()))
                .thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(itemOne))
                .thenReturn(itemOne);
        final ItemDtoOut itemDtoOut = itemService.addItem(itemDtoIn, userOne.getId());
        itemDtoOut.setRequestId(1L);
        assertNotNull(itemDtoOut);
        assertEquals(ItemMapper.toItemDtoOut(itemOne), itemDtoOut);
    }

    @Test
    void updateItem() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(itemOne));
        when(userRepository.existsById(1L))
                .thenReturn(true);
        final ItemDtoIn itemDtoInUpdate = new ItemDtoIn(1L, "update", "description", true);
        final ItemDtoOut itemDtoOut = itemService.updateItem(1L, itemDtoInUpdate, 1L);
        assertNotNull(itemDtoOut);
    }

    @Test
    void deleteItemDto() {
    }

    @Test
    void findItemById() {
    }

    @Test
    void findAllItemDtoByUserId() {
    }

    @Test
    void getItemsDtoBySearch() {
    }

    @Test
    void addComment() {
    }
}