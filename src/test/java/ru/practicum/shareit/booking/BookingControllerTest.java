package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    private BookingDtoOut bookingDtoOut;
    private BookingDtoIn bookingDtoIn;

    @BeforeEach
    void beforeEach() {
        bookingDtoIn = new BookingDtoIn(1L, 1L, LocalDateTime.now(),
                LocalDateTime.now(),
                new Item(1L, "item", "description", true),
                new User(1L, "user@user.com", "user"), Status.WAITING);
        bookingDtoOut = new BookingDtoOut(1L, LocalDateTime.now(),
                LocalDateTime.now(), Status.APPROVED,
                new BookingDtoOut.UserDto(1L),
                new BookingDtoOut.ItemDto(1L, "item"));
    }

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(bookingDtoIn, 1L))
                .thenReturn(bookingDtoOut);
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoOut)));
        verify(bookingService, Mockito.times(1))
                .addBooking(bookingDtoIn, 1L);
    }

    @Test
    void approvedBooking() throws Exception {
        when(bookingService.approvedBooking(1L, 1L, true))
                .thenReturn(bookingDtoOut);
        mockMvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoOut)));
        verify(bookingService, Mockito.times(1))
                .approvedBooking(1L, 1L, true);
    }

    @Test
    void getAllBookingsByUser() throws Exception {
        when(bookingService.getAllBookingsByBookerId(1L, "ALL",
                PageRequest.of(0, 10)))
                .thenReturn(List.of(bookingDtoOut));
        mockMvc.perform(get("/bookings?state=ALL")
                        .content(mapper.writeValueAsString(List.of(bookingDtoIn)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        List.of(bookingDtoOut))));
        verify(bookingService, Mockito.times(1))
                .getAllBookingsByBookerId(1L, "ALL",
                        PageRequest.of(0, 10));
    }

    @Test
    void getBookingsForAllItemsByUser() throws Exception {
        when(bookingService.getAllBookingsByOwnerId(1L, "ALL",
                PageRequest.of(0, 10)))
                .thenReturn(List.of(bookingDtoOut));
        mockMvc.perform(get("/bookings/owner?state=ALL")
                        .content(mapper.writeValueAsString(List.of(bookingDtoIn)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        List.of(bookingDtoOut))));
        verify(bookingService, Mockito.times(1))
                .getAllBookingsByOwnerId(1L, "ALL",
                        PageRequest.of(0, 10));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(1L, 1L))
                .thenReturn(bookingDtoOut);
        mockMvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        bookingDtoOut)));
        verify(bookingService, Mockito.times(1))
                .getBookingById(1L, 1L);
    }
}