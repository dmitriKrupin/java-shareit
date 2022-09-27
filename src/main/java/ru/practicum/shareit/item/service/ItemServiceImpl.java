package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, ItemRequestRepository itemRequestRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDtoOut addItem(ItemDtoIn itemDtoIn, Long userId) {
        Item item = ItemMapper.toItem(itemDtoIn);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нет такого пользователя с id " + userId));
        item.setOwner(user);
        if (itemDtoIn.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDtoIn.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Нет вещи с таким id " + itemDtoIn.getRequestId()));
            item.setItemRequest(itemRequest);
        }
        itemRepository.save(item);
        return ItemMapper.toItemDtoOut(item);
    }

    @Override
    public ItemDtoOut updateItem(long itemId, ItemDtoIn updateItemDtoIn, Long userId) {
        Item updateItem = ItemMapper.toItem(updateItemDtoIn);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Нет такой вещи с id " + itemId));
        if (userRepository.existsById(userId) && Objects.equals(item.getOwner().getId(), userId)) {
            if (updateItem.getName() != null) {
                item.setName(updateItem.getName());
            }
            if (updateItem.getDescription() != null) {
                item.setDescription(updateItem.getDescription());
            }
            if (updateItem.getAvailable() != null) {
                item.setAvailable(updateItem.getAvailable());
            }
            itemRepository.save(item);
            return ItemMapper.toItemDtoOut(item);
        } else {
            throw new NotFoundException("Нет такого пользователя с id " + userId);
        }
    }

    @Override
    public void deleteItemDto(Item itemForDelete) {
        itemRepository.delete(itemForDelete);
    }

    @Override
    public ItemDtoOutPost findItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Нет такой вещи с id " + itemId));
        List<Long> itemIdsList = new ArrayList<>();
        itemIdsList.add(itemId);

        List<Status> statuses = new ArrayList<>();
        statuses.add(Status.APPROVED);
        List<Booking> bookingsList = bookingRepository
                .findAllByItem_IdInAndStatusInOrderByEndDesc(itemIdsList, statuses);

        Booking lastBooking = new Booking();
        Booking nextBooking = new Booking();

        List<Comment> comments = commentRepository.findAllByItem_Id(itemId);
        List<CommentDtoOut> commentsDtoOutList = ItemMapper.toCommentDtoOut(comments);

        if (item.getOwner().getId() == userId && bookingsList.size() >= 2) {
            for (int i = 0; i < bookingsList.size() - 1; i++) {
                if (bookingsList.get(i).getEnd().isAfter(bookingsList.get(i + 1).getEnd())) {
                    lastBooking = bookingsList.get(i + 1);
                    nextBooking = bookingsList.get(i);
                } else {
                    lastBooking = bookingsList.get(i);
                    nextBooking = bookingsList.get(i + 1);
                }
            }
            return ItemMapper.toItemInfoDto(
                    item,
                    lastBooking,
                    nextBooking,
                    commentsDtoOutList);
        }
        if (item.getOwner().getId() == userId && bookingsList.size() == 1) {
            lastBooking = bookingsList.get(0);
            return ItemMapper.toItemInfoDto(
                    item,
                    lastBooking,
                    commentsDtoOutList);
        } else {
            return ItemMapper.toItemInfoDto(
                    item,
                    commentsDtoOutList);
        }
    }

    @Override
    public List<ItemDtoOutPost> findAllItemDtoByUserId(long userId) {
        List<Item> allItemsList = itemRepository.findAllByOwner_IdOrderById(userId);
        List<ItemDtoOutPost> allItemsDtoOutPostList = new ArrayList<>();
        for (Item entry : allItemsList) {
            allItemsDtoOutPostList.add(findItemById(entry.getId(), userId));
        }
        return allItemsDtoOutPostList;
    }

    @Override
    public List<ItemDtoOut> getItemsDtoBySearch(String text) {
        if (!text.isEmpty()) {
            List<Item> itemsList = itemRepository.findItemListBySearch(text);
            return ItemMapper.toItemsDtoOutList(itemsList);
        } else {
            System.out.println("Запрос пустой, повторите запрос!");
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDtoOut addComment(CommentDtoIn commentDtoIn, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id " + itemId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id " + userId));
        ArrayList<Status> statuses = new ArrayList<>();
        statuses.add(Status.APPROVED);
        statuses.add(Status.CANCELED);
        List<Booking> bookingsList = bookingRepository
                .findAllByBooker_IdAndStatusInOrderByEndDesc(userId, statuses);
        if (!userId.equals(item.getOwner().getId())
                && bookingsList.size() > 0) {
            int countOfBookingForItem = bookingsList.size();
            for (Booking entry : bookingsList) {
                if (entry.getEnd().isAfter(LocalDateTime.now())) {
                    countOfBookingForItem--;
                }
            }
            if (countOfBookingForItem > 0) {
                Comment comment = new Comment();
                comment.setItem(item);
                comment.setAuthor(user);
                comment.setText(commentDtoIn.getText());
                comment.setCreated(LocalDateTime.now());
                commentRepository.save(comment);
                return ItemMapper.toCommentDtoOut(comment);
            } else {
                throw new BadRequestException("Бронирование еще не завершено. Комментировать до конца бронирования нельзя!");
            }
        } else {
            throw new BadRequestException("У бронирования неверный пользователь!");
        }
    }
}
