package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDtoOut addRequest(ItemRequestDtoIn itemRequestDtoIn, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нет такого пользователя с id " + userId));
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDtoIn.getDescription());
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDtoOut(itemRequest);
    }

    @Override
    public List<ItemRequestDtoOut> getAllRequestsByUserId(long userId, PageRequest pageRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нет такого пользователя с id " + userId));
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestor_Id(userId, pageRequest);
        List<ItemDtoOut> itemsDtoOutList = getItemsDtoOutList(itemRequestList);
        return ItemRequestMapper.toItemRequestDtoOutListWithListItems(itemRequestList, itemsDtoOutList);
    }

    private List<ItemDtoOut> getItemsDtoOutList(List<ItemRequest> itemRequestList) {
        List<Item> itemsList = new ArrayList<>();
        for (ItemRequest entry : itemRequestList) {
            String[] searchText = entry.getDescription().split(" ");
            for (String text : searchText) {
                itemsList = itemRepository.findItemListBySearch(text);
            }
        }
        return ItemMapper.toItemsDtoOutList(itemsList);
    }

    @Override
    public List<ItemRequestDtoOut> getAllRequestsOtherUsers(Long ownerId, PageRequest pageRequest) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Нет такого ползователя с id " + ownerId));
        List<Item> itemsList = itemRepository.findAllByOwner_IdOrderById(user.getId());
        List<ItemRequestDtoOut> allRequestsOtherUsers;
        boolean isOwnerOfItem = true;
        long requestorId = ownerId;
        List<ItemRequest> itemRequestList = new ArrayList<>();
        if (itemsList.size() == 0) {
            itemRequestList = itemRequestRepository.findAllByRequestor_IdNot(requestorId, pageRequest);
        }
        for (Item entry : itemsList) {
            if (entry.getItemRequest() != null && !Objects.equals(entry.getItemRequest().getRequestor().getId(), ownerId)) {
                isOwnerOfItem = false;
                requestorId = entry.getItemRequest().getRequestor().getId();
            }
        }
        if (!isOwnerOfItem) {
            itemRequestList = itemRequestRepository.findAllByRequestor_Id(requestorId, pageRequest);
        }
        List<ItemDtoOut> itemsDtoOutList = getItemsDtoOutList(itemRequestList);
        allRequestsOtherUsers = ItemRequestMapper.toItemRequestDtoOutListWithListItems(itemRequestList, itemsDtoOutList);
        return allRequestsOtherUsers;
    }

    @Override
    public ItemRequestDtoOut getRequestById(long userId, long requestId, PageRequest pageRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нет такого пользователя с id " + userId));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Нет такого запроса с id " + requestId));
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestor_Id(requestId, pageRequest);
        List<ItemDtoOut> itemsDtoOutList = getItemsDtoOutList(itemRequestList);
        return ItemRequestMapper.toItemRequestDtoOutWithListItems(itemRequest, itemsDtoOutList);
    }
}
