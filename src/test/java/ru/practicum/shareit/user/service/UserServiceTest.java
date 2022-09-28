package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private User userOne;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        userOne = new User(1L, "userOne@user.com", "userOne");
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

        User userTwo = new User(2L, "userOne@user.com", "wrongUser");
        when(userRepository.save(userTwo))
                .thenThrow(new ConflictException("Такой адрес уже есть в базе!"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.addUser(UserMapper.toUserDto(userTwo));
        });
        String expectedMessage = "ConflictException";
        Class<? extends Exception> actualClass = exception.getClass();
        assertTrue(actualClass.getName().contains(expectedMessage));
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

    @Test
    void deleteUserDto() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userOne));
        userService.deleteUserDto(1L);
        Mockito.verify(userRepository, Mockito.times(1))
                .delete(userOne);
    }
}