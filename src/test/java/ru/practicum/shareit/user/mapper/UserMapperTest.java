package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {
    User user;
    UserDto userDto;
    List<User> usersList = new ArrayList<>();
    List<UserDto> userDtoList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user@user.com", "user");
        userDto = new UserDto(1L, "user@user.com", "user");
        usersList.add(user);
        userDtoList.add(userDto);
    }

    @Test
    void toUserDto() {
        UserDto userDtoForTest = UserMapper.toUserDto(user);
        assertNotNull(userDtoForTest);
        assertEquals(userDto, userDtoForTest);
    }

    @Test
    void toUser() {
        User userForTest = UserMapper.toUser(userDto);
        assertNotNull(userForTest);
        assertEquals(user, userForTest);
    }

    @Test
    void toUsersDtoList() {
        List<UserDto> userDtoListForTest = UserMapper.toUsersDtoList(usersList);
        assertNotNull(userDtoListForTest);
        assertEquals(userDtoList, userDtoListForTest);
    }

    @Test
    void toUsersList() {
        List<User> userListForTest = UserMapper.toUsersList(userDtoList);
        assertNotNull(userListForTest);
        assertEquals(usersList, userListForTest);
    }
}