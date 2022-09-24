package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    UserService userService;
    UserRepository userRepository;
    User userOne;
    User userTwo;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        userOne = new User(1L, "userOne@user.com", "userOne");
        userTwo = new User(2L, "userTwo@user.com", "userTwo");
    }

    @Test
    void getAllUsersDtoList() {
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(userOne));
        final List<UserDto> userDtoList = userService.getAllUsersDtoList();
        assertNotNull(userDtoList);
        assertEquals(1, userDtoList.size());
        assertEquals(UserMapper.toUserDto(userOne), userDtoList.get(0));
    }

    @Test
    void findUserById() {
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.of(userOne));
        final UserDto userDto = userService.findUserById(userOne.getId());
        assertNotNull(userDto);
        assertEquals(userOne, UserMapper.toUser(userDto));
    }

    @Test
    void addUser() {
        when(userRepository.save(userOne))
                .thenReturn(userOne);
        final UserDto userDto = userService.addUser(UserMapper.toUserDto(userOne));
        assertNotNull(userDto);
        assertEquals(userOne, UserMapper.toUser(userDto));
    }

    @Test
    void updateUser() {
        userOne.setName("update");
        userOne.setEmail("updateUserOne@user.com");
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.of(userOne));
        when(userRepository.save(userOne))
                .thenReturn(userOne);
        final UserDto userDto = userService.updateUser(UserMapper.toUserDto(userOne));
        assertNotNull(userDto);
        assertEquals(userOne, UserMapper.toUser(userDto));
    }

}