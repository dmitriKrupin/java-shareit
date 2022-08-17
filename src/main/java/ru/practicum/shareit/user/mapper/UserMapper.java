package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
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

    public static Collection<UserDto> toUsersListDto(List<User> usersList) {
        Collection<UserDto> usersDtoList = new ArrayList<>();
        for (User entry : usersList) {
            usersDtoList.add(UserMapper.toUserDto(entry));
        }
        return usersDtoList;
    }
}
