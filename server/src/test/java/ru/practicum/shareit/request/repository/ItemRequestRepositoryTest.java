package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User userOne;
    private User userTwo;
    private ItemRequest itemRequestOne;
    private ItemRequest itemRequestTwo;

    @BeforeEach
    void beforeEach() {
        userOne = userRepository.save(
                new User(1L, "userOne@user.com", "userOne"));
        userTwo = userRepository.save(
                new User(2L, "userTwo@user.com", "userTwo"));
        itemRequestOne = itemRequestRepository.save(
                new ItemRequest(1L, "itemRequestOneDescription",
                        userOne, LocalDateTime.now()));
        itemRequestTwo = itemRequestRepository.save(
                new ItemRequest(2L, "itemRequestTwoDescription",
                        userTwo, LocalDateTime.now()));
    }

    @Test
    void findAllByRequestor_Id() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestor_Id(userOne.getId());
        assertNotNull(itemRequestList, "Список пуст!");
        assertEquals(1, itemRequestList.size(), "В списке неверное кол-во запросов!");
        assertEquals(itemRequestOne.getDescription(), itemRequestList.get(0).getDescription(), "Неправильное описание!");
    }

    @Test
    void findAllByRequestor_IdNot() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestor_IdNot(
                userOne.getId(), PageRequest.of(0, 10));
        assertNotNull(itemRequestList, "Список пуст!");
        assertEquals(1, itemRequestList.size(), "В списке неверное кол-во запросов!");
        assertEquals(itemRequestTwo.getDescription(), itemRequestList.get(0).getDescription(), "Неправильное описание!");
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}