package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceTest {
    private ItemRepository itemRepository;
    private ItemService itemService;
    private UserRepository userRepository;
    private ItemRequestRepository itemRequestRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private User userOne;
    private User userTwo;
    private Item itemOne;
    private Item itemTwo;
    private ItemRequest itemRequestOne;
    private ItemRequest itemRequestTwo;
    private Booking bookingOne;
    private Booking bookingTwo;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, itemRequestRepository,
                bookingRepository, commentRepository);
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
        bookingOne = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                itemTwo, userOne, Status.APPROVED);
        bookingTwo = new Booking(2L, LocalDateTime.now().minusHours(24), LocalDateTime.now().minusHours(22),
                itemOne, userTwo, Status.APPROVED);
        comment = new Comment(1L, "text of comment", itemOne, userTwo,
                LocalDateTime.now());
    }

    @Test
    void addItem() {
        ItemDtoIn itemDtoIn = ItemMapper.toItemDtoIn(itemOne);
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.of(userOne));
        itemDtoIn.setRequestId(1L);
        when(itemRequestRepository.findById(itemDtoIn.getRequestId()))
                .thenReturn(Optional.of(itemRequestOne));
        when(itemRepository.save(itemOne))
                .thenReturn(itemOne);
        final ItemDtoOut itemDtoOut = itemService.addItem(itemDtoIn, userOne.getId());
        itemDtoOut.setRequestId(1L);
        assertNotNull(itemDtoOut);
        assertEquals(ItemMapper.toItemDtoOut(itemOne), itemDtoOut);
    }

    @Test
    void updateItem() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(itemOne));
        when(userRepository.existsById(1L))
                .thenReturn(true);
        final ItemDtoIn itemDtoInUpdate = new ItemDtoIn(1L, "update", "description", true);
        final ItemDtoOut itemDtoOut = itemService.updateItem(1L, itemDtoInUpdate, 1L);
        assertNotNull(itemDtoOut);
        assertEquals(ItemMapper.toItemDtoOut(itemOne), itemDtoOut);

        when(userRepository.existsById(1L))
                .thenReturn(false);
        Exception oneException = assertThrows(RuntimeException.class, () -> {
            itemService.updateItem(1L, itemDtoInUpdate, 1L);
        });
        String oneExpectedMessage = "NotFoundException";
        Class<? extends Exception> oneActualClass = oneException.getClass();
        assertTrue(oneActualClass.getName().contains(oneExpectedMessage));
    }

    @Test
    void deleteItemDto() {
        itemService.deleteItemDto(itemOne);
        Mockito.verify(itemRepository, Mockito.times(1))
                .delete(itemOne);
    }

    @Test
    void findItemById() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(itemOne));
        when(bookingRepository.findAllByItem_IdInAndStatusInOrderByEndDesc(List.of(1L), List.of(Status.APPROVED)))
                .thenReturn(List.of(bookingOne));
        when(commentRepository.findAllByItem_Id(1L))
                .thenReturn(List.of(comment));
        final ItemDtoOutPost itemDtoOutPost = itemService.findItemById(1L, 1L);
        assertNotNull(itemDtoOutPost);
        assertEquals(bookingOne.getEnd(), itemDtoOutPost.getLastBooking().getEnd());
    }

    @Test
    void findAllItemDtoByUserId() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(itemOne));
        when(itemRepository.findAllByOwner_IdOrderById(1L))
                .thenReturn(List.of(itemOne));
        List<ItemDtoOutPost> itemDtoOutPostList = itemService.findAllItemDtoByUserId(1L);
        when(itemRepository.findById(4L))
                .thenThrow(new NotFoundException("Нет такой вещи с id " + 4L));
        List<ItemDtoOutPost> notItemDtoOutPostList = itemService.findAllItemDtoByUserId(4L);

        assertNotNull(itemDtoOutPostList);
        assertEquals(1, itemDtoOutPostList.size());
        assertEquals(0, notItemDtoOutPostList.size());
    }

    @Test
    void getItemsDtoBySearch() {
        when(itemRepository.findItemListBySearch("item"))
                .thenReturn(List.of(itemOne));
        List<ItemDtoOut> itemDtoOutList = itemService.getItemsDtoBySearch("item");
        assertNotNull(itemDtoOutList);
        assertEquals(ItemMapper.toItemDtoOut(itemOne), itemDtoOutList.get(0));
    }

    @Test
    void addComment() {
        CommentDtoIn commentDtoIn = ItemMapper.toCommentDtoIn(comment);
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(itemOne));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(userTwo));
        when(bookingRepository.findAllByBooker_IdAndStatusInOrderByEndDesc(
                2L, List.of(Status.APPROVED, Status.CANCELED))).thenReturn(List.of(bookingTwo));
        when(commentRepository.save(comment))
                .thenReturn(comment);

        CommentDtoOut commentDtoOut = itemService.addComment(commentDtoIn,
                1L, 2L);
        assertNotNull(commentDtoOut);
        assertEquals(commentDtoIn.getText(), commentDtoOut.getText());

        bookingTwo.setEnd(LocalDateTime.now().plusHours(100));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            itemService.addComment(commentDtoIn, 1L, 2L);
        });
        String expectedMessage = "BadRequestException";
        Class<? extends Exception> actualClass = exception.getClass();
        assertTrue(actualClass.getName().contains(expectedMessage));


    }
}