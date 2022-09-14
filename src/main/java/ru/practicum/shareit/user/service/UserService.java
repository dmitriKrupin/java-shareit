package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsersDtoList();

    UserDto findUserById(long userDtoId);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto updateUserDto);

    void deleteUserDto(Long userDtoId);
}
