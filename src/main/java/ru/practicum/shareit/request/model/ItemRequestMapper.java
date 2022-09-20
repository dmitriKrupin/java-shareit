package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestMapper {
    public static ItemRequestDtoOut toItemRequestDtoOut(
            ItemRequest itemRequest) {
        return new ItemRequestDtoOut(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated());
    }

    public static ItemRequestDtoOut toItemRequestDtoOutWithListItems(
            ItemRequest itemRequest,
            List<ItemDtoOut> items) {
        return new ItemRequestDtoOut(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items);
    }

    public static List<ItemRequestDtoOut> toItemRequestDtoOutList(List<ItemRequest> itemRequestsList) {
        List<ItemRequestDtoOut> itemRequestDtoOutList = new ArrayList<>();
        for (ItemRequest entry : itemRequestsList) {
            itemRequestDtoOutList.add(toItemRequestDtoOut(entry));
        }
        return itemRequestDtoOutList;
    }

    public static List<ItemRequestDtoOut> toItemRequestDtoOutListWithListItems(
            List<ItemRequest> itemRequestsList, List<ItemDtoOut> items) {
        List<ItemRequestDtoOut> itemRequestDtoOutList = new ArrayList<>();
        for (ItemRequest entry : itemRequestsList) {
            itemRequestDtoOutList.add(toItemRequestDtoOutWithListItems(entry, items));
        }
        return itemRequestDtoOutList;
    }
}
