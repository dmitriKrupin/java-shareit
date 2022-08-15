package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersListMapper {
    UsersListMapper INSTANCE = Mappers.getMapper(UsersListMapper.class);

    List<UserDto> toDTOList(List<User> usersList);

    List<User> toModelList(List<UserDto> userDtosList);
}
