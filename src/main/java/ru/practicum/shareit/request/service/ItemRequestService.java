package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut addRequest(ItemRequestDtoIn itemRequestDtoIn, long userId);

    List<ItemRequestDtoOut> getAllRequestsByUserId(long userId, PageRequest pageRequest);

    List<ItemRequestDtoOut> getAllRequestsOtherUsers(Long ownerId, PageRequest pageRequest);

    ItemRequestDtoOut getRequestById(long userId, long requestId);
}
