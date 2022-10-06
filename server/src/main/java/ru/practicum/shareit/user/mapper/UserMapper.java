package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getName()
        );
    }

    public static List<UserDto> toUsersDtoList(List<User> usersList) {
        List<UserDto> usersDtoList = new ArrayList<>();
        for (User entry : usersList) {
            usersDtoList.add(UserMapper.toUserDto(entry));
        }
        return usersDtoList;
    }

    public static List<User> toUsersList(List<UserDto> usersDtoList) {
        List<User> usersList = new ArrayList<>();
        for (UserDto entry : usersDtoList) {
            usersList.add(UserMapper.toUser(entry));
        }
        return usersList;
    }
}
