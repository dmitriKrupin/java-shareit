package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.item.dto.ItemDtoOutPatch;
import ru.practicum.shareit.item.dto.ItemDtoOutPost;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDtoOutPost.BookingDto addBooking(BookingDtoIn bookingDtoIn, String userId) {
        Booking booking = BookingMapper.toBooking(bookingDtoIn);
        booking.setBooker(userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("Нет такого пользователя с id " + userId)));
        Long itemId = bookingDtoIn.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Нет такой вещи с id " + itemId));
        if (item.getOwner().getId() == Long.parseLong(userId)) {
            throw new NotFoundException("В аренду нельзя взять свое оборудование!");
        }
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        if (item.getAvailable()
                && !booking.getStart().isBefore(LocalDateTime.now())
                && !booking.getStart().isAfter(booking.getEnd())
                && !booking.getEnd().isBefore(booking.getStart())) {
            bookingRepository.save(booking);
            return BookingMapper.toBookingDtoForPostOutput(booking);
        } else {
            throw new BadRequestException("Вещь с id " + itemId + " занята!");
        }
    }

    @Override
    public ItemDtoOutPatch.BookingDto approvedBooking(String userId, Long bookingId, Boolean approved) {
        List<Item> itemsList = itemRepository.findAllByOwner_IdOrderById(Long.parseLong(userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Нет такого бронирования с id " + bookingId));
        if (itemsList.contains(booking.getItem())
                && booking.getItem().getOwner().getId() == Long.parseLong(userId)) {
            if (approved) {
                if (booking.getStatus() != Status.APPROVED) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    throw new BadRequestException("Статус уже поменяли на " + Status.APPROVED);
                }
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.save(booking);
            return BookingMapper.toBookingDtoForPatchOutput(booking);
        } else {
            throw new NotFoundException("Нет такой вещи с id " + booking.getItem().getId());
        }
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByBookerId(String bookerId, String state) {
        long bookerIdFromString = Long.parseLong(bookerId);
        if (userRepository.existsById(bookerIdFromString)) {
            List<Booking> bookingsList;
            if (state == null || state.equals(Status.ALL.toString())) {
                bookingsList = bookingRepository.findAllByBooker_IdOrderByEndDesc(
                        bookerIdFromString);
            } else {
                bookingsList = bookingRepository.findAllByBooker_IdAndStatusInOrderByEndDesc(
                        bookerIdFromString, getListOfStatus(state));
                if (state.equals(Status.PAST.toString())) {
                    bookingsList = bookingsList.subList(bookingsList.size() - 1, bookingsList.size());
                }
            }
            return BookingMapper.toBookingDtosList(bookingsList);
        } else {
            throw new NotFoundException("Нет такого пользователя с id " + bookerIdFromString);
        }
    }

    private ArrayList<Status> getListOfStatus(String state) {
        ArrayList<Status> statuses = new ArrayList<>();
        Status stateFromString;
        try {
            stateFromString = Status.valueOf(state);
        } catch (Exception e) {
            throw new UnsupportedException("Unknown state: " + state);
        }
        switch (stateFromString) {
            case CURRENT:
                statuses.add(Status.REJECTED);
                break;
            case FUTURE:
                statuses.add(Status.WAITING);
                statuses.add(Status.APPROVED);
                break;
            case PAST:
                statuses.add(Status.CANCELED);
                statuses.add(Status.APPROVED);
                break;
            default:
                try {
                    statuses.add(Status.valueOf(state));
                    break;
                } catch (UnsupportedException e) {
                    throw new UnsupportedException("Unknown state: " + state);
                }
        }
        return statuses;
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByOwnerId(String ownerId, String state) {
        long ownerIdFromString = Long.parseLong(ownerId);
        if (userRepository.existsById(ownerIdFromString)) {
            List<Booking> bookingsList;
            List<Long> ownerIdsList = new ArrayList<>();
            List<Item> item = itemRepository.findAllByOwner_IdOrderById(ownerIdFromString);
            for (Item entry : item) {
                ownerIdsList.add(entry.getId());
            }
            if (state == null || state.equals(Status.ALL.toString())) {
                bookingsList = bookingRepository.findAllByItem_IdInOrderByEndDesc(ownerIdsList);
            } else {
                bookingsList = bookingRepository.findAllByItem_IdInAndStatusInOrderByEndDesc(
                        ownerIdsList, getListOfStatus(state));
                if (state.equals(Status.PAST.toString())) {
                    bookingsList = bookingsList.subList(bookingsList.size() - 1, bookingsList.size());
                }
            }
            return BookingMapper.toBookingDtosList(bookingsList);
        } else {
            throw new NotFoundException("Нет такого пользователя с id " + ownerIdFromString);
        }
    }

    @Override
    public BookingDtoOut getBookingById(long bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Нет такого бронирования с id " + bookingId));
        if (booking.getItem().getOwner().getId() == Long.parseLong(userId)
                || booking.getBooker().getId() == Long.parseLong(userId)) {
            return BookingMapper.toBookingDtoOut(booking);
        } else {
            throw new NotFoundException("У этого бронирования другой арендатор");
        }
    }
}
