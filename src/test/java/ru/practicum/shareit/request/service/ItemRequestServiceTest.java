package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemRequestServiceTest {
    private ItemRequestService itemRequestService;
    private ItemRequestRepository itemRequestRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ItemRequest itemRequestOne;
    private ItemRequest itemRequestTwo;
    private User userOne;
    private User userTwo;
    private Item itemOne;
    private Item itemTwo;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository,
                userRepository, itemRepository);
        userOne = new User(1L, "userOne@user.com", "userOne");
        userTwo = new User(2L, "userTwo@user.com", "userTwo");
        itemRequestOne = new ItemRequest(1L, "request one from userOne",
                userOne, LocalDateTime.of(2022, 9, 25, 14,
                17, 3));
        itemOne = new Item(1L, "item one", "item one description",
                true, userOne, itemRequestOne);
    }

    @Test
    void addRequest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userOne));
        when(itemRequestRepository.save(itemRequestOne))
                .thenReturn(itemRequestOne);
        final ItemRequestDtoIn itemRequestDtoIn = ItemRequestMapper.toItemRequestDtoIn(itemRequestOne);
        final ItemRequestDtoOut itemRequestDtoOut = itemRequestService.addRequest(
                itemRequestDtoIn, 1L);
        assertNotNull(itemRequestDtoOut);
        itemRequestDtoOut.setId(itemRequestOne.getId());
        itemRequestDtoOut.setCreated(itemRequestOne.getCreated());
        assertEquals(ItemRequestMapper.toItemRequestDtoOut(itemRequestOne), itemRequestDtoOut);
    }

    @Test
    void getAllRequestsByUserId() {
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequestOne);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userOne));
        when(itemRequestRepository.findAllByRequestor_Id(1L,
                PageRequest.of(0, 10))).thenReturn(itemRequestList);
        List<ItemRequestDtoOut> itemRequestDtoOutList = itemRequestService
                .getAllRequestsByUserId(1L, PageRequest.of(0, 10));
        assertNotNull(itemRequestDtoOutList);
        assertEquals(1, itemRequestDtoOutList.size());
        itemRequestDtoOutList.get(0).setItems(null);
        assertEquals(ItemRequestMapper.toItemRequestDtoOut(itemRequestOne),
                itemRequestDtoOutList.get(0));
    }

    @Test
    void getAllRequestsOtherUsers() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userOne));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(userTwo));
        when(itemRepository.findAllByOwner_IdOrderById(1L))
                .thenReturn(List.of(itemOne));
        when(itemRequestRepository.findAllByRequestor_IdNot(2L,
                PageRequest.of(0, 10))).thenReturn(List.of(itemRequestOne));

        List<ItemRequestDtoOut> itemRequestDtoOutList = itemRequestService.getAllRequestsOtherUsers(
                2L, PageRequest.of(0, 10));

        assertNotNull(itemRequestDtoOutList);
        assertEquals(1, itemRequestDtoOutList.size());
        itemRequestDtoOutList.get(0).setItems(null);
        assertEquals(ItemRequestMapper.toItemRequestDtoOut(itemRequestOne),
                itemRequestDtoOutList.get(0));
    }

    @Test
    void getRequestById() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userOne));
        when(itemRequestRepository.findById(1L))
                .thenReturn(Optional.of(itemRequestOne));
        when(itemRequestRepository.findAllByRequestor_Id(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(itemRequestOne));
        ItemRequestDtoOut itemRequestDtoOut = itemRequestService.getRequestById(1L, 1L, PageRequest.of(0, 10));
        assertNotNull(itemRequestDtoOut);
        itemRequestDtoOut.setItems(null);
        assertEquals(ItemRequestMapper.toItemRequestDtoOut(itemRequestOne),
                itemRequestDtoOut);
    }
}