package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getAllUsersDtoList();

    UserDto findUserById(long userDtoId);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto updateUserDto);

    void deleteUserDto(Long userDtoId);
}
