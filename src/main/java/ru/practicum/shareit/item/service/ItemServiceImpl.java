package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, RequestRepository requestRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDtoIn addItem(ItemDtoIn itemDtoIn, String userId) {
        Item item = ItemMapper.toItem(itemDtoIn);
        if (userRepository.existsById(Long.parseLong(userId))) {
            ItemRequest request = new ItemRequest();
            request.setDescription(item.getDescription());
            request.setRequestor(userRepository.getReferenceById(Long.parseLong(userId)));
            request.setCreated(LocalDate.now());
            requestRepository.save(request);
            item.setOwner(userRepository.getReferenceById(Long.parseLong(userId)));
            item.setRequest(request);
            itemRepository.save(item);
            return ItemMapper.toItemDto(item);
        } else {
            throw new NotFoundException("Нет такого пользователя с id " + userId);
        }
    }

    @Override
    public ItemDtoIn updateItem(long itemId, ItemDtoIn updateItemDtoIn, String userId) {
        Item updateItem = ItemMapper.toItem(updateItemDtoIn);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Нет такой вещи с id " + itemId));
        if (userRepository.existsById(Long.parseLong(userId)) && item.getOwner().getId() == Long.parseLong(userId)) {
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
            return ItemMapper.toItemDto(item);
        } else {
            throw new NotFoundException("Нет такого пользователя с id " + userId);
        }
    }

    @Override
    public void deleteItemDto(Item itemForDelete) {
        itemRepository.delete(itemForDelete);
    }

    @Override
    public ItemDtoOutPost findItemById(long itemId, String userId) {
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

        if (item.getOwner().getId() == Long.parseLong(userId) && bookingsList.size() >= 2) {
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
        if (item.getOwner().getId() == Long.parseLong(userId) && bookingsList.size() == 1) {
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
            allItemsDtoOutPostList.add(findItemById(entry.getId(), String.valueOf(userId)));
        }
        return allItemsDtoOutPostList;
    }

    @Override
    public List<ItemDtoIn> getItemsDtoBySearch(String text) {
        if (!text.isEmpty()) {
            List<Item> itemsList = itemRepository.findItemListBySearch(text);
            return ItemMapper.toItemsListDto(itemsList);
        } else {
            System.out.println("Запрос пустой, повторите запрос!");
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDtoOut addComment(CommentDtoIn commentDtoIn, long itemId, String userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id " + itemId));
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("Нет пользователя с таким id " + userId));
        ArrayList<Status> statuses = new ArrayList<>();
        statuses.add(Status.APPROVED);
        statuses.add(Status.CANCELED);
        List<Booking> bookingsList = bookingRepository
                .findAllByBooker_IdAndStatusInOrderByEndDesc(
                        Long.parseLong(userId), statuses);
        if (Long.parseLong(userId) != item.getOwner().getId()
                && bookingsList.size() > 1) {
            Comment comment = new Comment();
            comment.setItem(item);
            comment.setAuthor(user);
            comment.setText(commentDtoIn.getText());
            comment.setCreated(LocalDateTime.now());
            commentRepository.save(comment);
            return ItemMapper.toCommentDtoOut(comment);
        } else {
            throw new BadRequestException("У бронирования неверный пользователь!");
        }
    }
}
