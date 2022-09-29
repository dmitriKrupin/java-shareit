package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private User userOne;
    private User userTwo;
    private Item itemOne;
    private Item itemTwo;
    private ItemRequest itemRequestOne;
    private ItemRequest itemRequestTwo;
    private Booking bookingOne;
    private Booking bookingTwo;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository,
                itemRepository, userRepository);
        userOne = new User(1L, "userOne@user.com", "userOne");
        userTwo = new User(2L, "userTwo@user.com", "userTwo");
        itemRequestOne = new ItemRequest(1L, "itemRequestOneDescription",
                userTwo, LocalDateTime.now());
        itemRequestTwo = new ItemRequest(2L, "itemRequestTwoDescription",
                userOne, LocalDateTime.now());
        itemOne = new Item(1L, "itemNameOne", "itemOneDescription",
                true, userOne, itemRequestOne);
        itemTwo = new Item(2L, "itemNameTwo", "itemTwoDescription",
                true, userTwo, itemRequestTwo);
        bookingOne = new Booking(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(1),
                itemTwo, userOne, Status.WAITING);
        bookingTwo = new Booking(2L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                itemOne, userTwo, Status.APPROVED);
    }

    @Test
    void addBooking() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userOne));
        when(itemRepository.findById(2L))
                .thenReturn(Optional.of(itemTwo));
        when(bookingRepository.save(bookingOne))
                .thenReturn(bookingOne);
        BookingDtoOut bookingDtoOut = bookingService.addBooking(
                BookingMapper.toBookingDtoIn(bookingOne), 1L);
        assertNotNull(bookingDtoOut);
        assertEquals(BookingMapper.toBookingDtoOut(bookingOne).getStart(),
                bookingDtoOut.getStart());

        itemTwo.setOwner(userOne);
        Exception oneException = assertThrows(RuntimeException.class, () -> {
            bookingService.addBooking(
                    BookingMapper.toBookingDtoIn(bookingOne), 1L);
        });
        String oneExpectedMessage = "NotFoundException";
        Class<? extends Exception> oneActualClass = oneException.getClass();
        assertTrue(oneActualClass.getName().contains(oneExpectedMessage));

        itemTwo.setOwner(userTwo);
        itemTwo.setAvailable(false);
        Exception secondException = assertThrows(RuntimeException.class, () -> {
            bookingService.addBooking(
                    BookingMapper.toBookingDtoIn(bookingOne), 1L);
        });
        String secondExpectedMessage = "BadRequestException";
        Class<? extends Exception> secondActualClass = secondException.getClass();
        assertTrue(secondActualClass.getName().contains(secondExpectedMessage));
    }

    @Test
    void approvedBooking() {
        when(itemRepository.findAllByOwner_IdOrderById(2L))
                .thenReturn(List.of(itemTwo));
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(bookingOne));
        when(bookingRepository.save(bookingOne))
                .thenReturn(bookingOne);

        BookingDtoOut bookingDtoOut = bookingService.approvedBooking(
                2L, 1L, true);
        assertNotNull(bookingDtoOut);
        assertEquals(Status.APPROVED, bookingDtoOut.getStatus());

        Exception oneException = assertThrows(RuntimeException.class, () -> {
            bookingService.approvedBooking(
                    2L, 1L, true);
        });
        String oneExpectedMessage = "BadRequestException";
        Class<? extends Exception> oneActualClass = oneException.getClass();
        assertTrue(oneActualClass.getName().contains(oneExpectedMessage));

        when(itemRepository.findAllByOwner_IdOrderById(2L))
                .thenReturn(List.of(itemOne));
        Exception secondException = assertThrows(RuntimeException.class, () -> {
            bookingService.approvedBooking(
                    2L, 1L, true);
        });
        String secondExpectedMessage = "NotFoundException";
        Class<? extends Exception> secondActualClass = secondException.getClass();
        assertTrue(secondActualClass.getName().contains(secondExpectedMessage));
    }

    @Test
    void getAllBookingsByBookerId() {
        Long bookerId = 1L;
        String state = "FUTURE";
        ArrayList<Status> statusArrayList = new ArrayList<>();
        statusArrayList.add(Status.WAITING);
        statusArrayList.add(Status.APPROVED);
        when(userRepository.existsById(bookerId))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdOrderByEndDesc(
                bookerId, PageRequest.of(0, 10)))
                .thenReturn(List.of(bookingOne));
        when(bookingRepository.findAllByBooker_IdAndStatusInOrderByEndDesc(
                bookerId, statusArrayList, PageRequest.of(0, 10)))
                .thenReturn(List.of(bookingTwo));

        List<BookingDtoOut> bookingDtoOutList = bookingService
                .getAllBookingsByBookerId(bookerId, state, PageRequest.of(0, 10));
        assertNotNull(bookingDtoOutList);
        assertEquals(Status.APPROVED, bookingDtoOutList.get(0).getStatus());
        assertEquals(1, bookingDtoOutList.size());

        List<BookingDtoOut> withNullStateBookingDtoOutList = bookingService
                .getAllBookingsByBookerId(bookerId, null, PageRequest.of(0, 10));
        assertNotNull(withNullStateBookingDtoOutList);
        assertEquals(1, withNullStateBookingDtoOutList.size());

        bookingTwo.setStatus(Status.CANCELED);
        when(bookingRepository.findAllByBooker_IdAndStatusInOrderByEndDesc(
                bookerId, List.of(Status.CANCELED, Status.APPROVED),
                PageRequest.of(0, 10)))
                .thenReturn(List.of(bookingTwo));
        List<BookingDtoOut> withPastStateBookingDtoOutList = bookingService
                .getAllBookingsByBookerId(bookerId, "PAST", PageRequest.of(0, 10));
        assertNotNull(withPastStateBookingDtoOutList);
        assertEquals(1, withPastStateBookingDtoOutList.size());

        when(userRepository.existsById(bookerId))
                .thenReturn(false);
        Exception oneException = assertThrows(RuntimeException.class, () -> {
            bookingService.getAllBookingsByBookerId(
                    bookerId, state, PageRequest.of(0, 10));
        });
        String oneExpectedMessage = "NotFoundException";
        Class<? extends Exception> oneActualClass = oneException.getClass();
        assertTrue(oneActualClass.getName().contains(oneExpectedMessage));
    }

    @Test
    void getAllBookingsByOwnerId() {
        long ownerIdFromString = 1L;
        String state = "PAST";
        ArrayList<Status> statusArrayList = new ArrayList<>();
        statusArrayList.add(Status.CANCELED);
        statusArrayList.add(Status.APPROVED);
        bookingOne.setStatus(Status.CANCELED);
        bookingTwo.setStatus(Status.CANCELED);
        when(userRepository.existsById(ownerIdFromString))
                .thenReturn(true);
        when(itemRepository.findAllByOwner_IdOrderById(ownerIdFromString))
                .thenReturn(List.of(itemOne));
        when(bookingRepository.findAllByItem_IdInOrderByEndDesc(List.of(ownerIdFromString),
                PageRequest.of(0, 10))).thenReturn(List.of(bookingOne));
        when(bookingRepository.findAllByItem_IdInAndStatusInOrderByEndDesc(
                List.of(ownerIdFromString), statusArrayList,
                PageRequest.of(0, 10))).thenReturn(List.of(bookingTwo));

        List<BookingDtoOut> bookingDtoOutList = bookingService.getAllBookingsByOwnerId(
                ownerIdFromString, state, PageRequest.of(0, 10));
        assertNotNull(bookingDtoOutList);
        assertEquals(Status.CANCELED, bookingDtoOutList.get(0).getStatus());
        assertEquals(1, bookingDtoOutList.size());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.getAllBookingsByOwnerId(ownerIdFromString, "APPROVE", PageRequest.of(0, 10));
        });
        String expectedMessage = "UnsupportedException";
        Class<? extends Exception> actualClass = exception.getClass();
        assertTrue(actualClass.getName().contains(expectedMessage));

        List<BookingDtoOut> withNullBookingDtoOutList = bookingService.getAllBookingsByOwnerId(
                ownerIdFromString, null, PageRequest.of(0, 10));
        assertNotNull(withNullBookingDtoOutList);
        assertEquals(1, withNullBookingDtoOutList.size());

        when(userRepository.existsById(ownerIdFromString))
                .thenReturn(false);
        Exception oneException = assertThrows(RuntimeException.class, () -> {
            bookingService.getAllBookingsByOwnerId(
                    ownerIdFromString, state, PageRequest.of(0, 10));
        });
        String oneExpectedMessage = "NotFoundException";
        Class<? extends Exception> oneActualClass = oneException.getClass();
        assertTrue(oneActualClass.getName().contains(oneExpectedMessage));
    }

    @Test
    void getBookingById() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(bookingOne));
        BookingDtoOut bookingDtoOut = bookingService.getBookingById(1L, 1L);
        assertNotNull(bookingDtoOut);
        assertEquals(BookingMapper.toBookingDtoOut(bookingOne).getStart(),
                bookingDtoOut.getStart());

        bookingOne.setBooker(userTwo);
        Exception oneException = assertThrows(RuntimeException.class, () -> {
            bookingService.getBookingById(1L, 1L);
        });
        String oneExpectedMessage = "NotFoundException";
        Class<? extends Exception> oneActualClass = oneException.getClass();
        assertTrue(oneActualClass.getName().contains(oneExpectedMessage));
    }
}