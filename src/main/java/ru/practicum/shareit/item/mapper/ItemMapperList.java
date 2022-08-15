package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapperList {

    ItemMapperList INSTANCE = Mappers.getMapper(ItemMapperList.class);

    List<ItemDto> toDTOList(List<Item> usersList);

    List<Item> toModelList(List<ItemDto> userDtosList);
}
