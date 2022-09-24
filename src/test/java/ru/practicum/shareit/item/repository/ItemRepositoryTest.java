package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    User user1;
    User user2;
    Item item1;
    Item item2;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(1L, "one@user.com", "user1"));
        item1 = itemRepository.save(new Item(1L, "item1", "Description of item1", false));
        item1.setOwner(user1);

        user2 = userRepository.save(new User(2L, "two@user.com", "user2"));
        item2 = itemRepository.save(new Item(2L, "item2", "Description of item2", false));
        item2.setOwner(user2);
    }

    @Test
    void findAllByOwner_IdOrderById() {
        List<Item> itemsList = itemRepository.findAllByOwner_IdOrderById(user1.getId());
        assertNotNull(itemsList, "Список пуст!");
        assertEquals(1, itemsList.size(), "В списке неверное кол-во вещей!");
        assertEquals("item1", itemsList.get(0).getName(), "Неправильное имя вещи!");
        Item secondItem = itemRepository.findAllByOwner_IdOrderById(user2.getId()).get(0);
        itemsList.add(secondItem);
        assertNotNull(itemsList, "Список пуст!");
        assertEquals(2, itemsList.size(), "В списке неверное кол-во вещей!");
        assertEquals("item2", itemsList.get(1).getName(), "Неправильное имя вещи!");
    }

    @Test
    void findItemListBySearch() {
        List<Item> itemsSearchList = itemRepository.findItemListBySearch("item1");
        assertNotNull(itemsSearchList, "Список пуст!");
        assertEquals(1, itemsSearchList.size(), "В списке неверное кол-во вещей!");
        assertEquals("Description of item1", itemsSearchList.get(0).getDescription(),
                "Неправильное имя вещи!");
        Item secondItem = itemRepository.findItemListBySearch("item2").get(0);
        itemsSearchList.add(secondItem);
        assertEquals(2, itemsSearchList.size(), "В списке неверное кол-во вещей!");
        assertEquals("Description of item2", itemsSearchList.get(1).getDescription(),
                "Неправильное имя вещи!");
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }
}