package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                LocalDateTime.now(), new Item(), new User(), Status.APPROVED);
        bookingDtoOut = new BookingDtoOut(1L, LocalDateTime.now(),
                LocalDateTime.now(), 1L);
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
    void approvedBooking() {
    }

    @Test
    void getAllBookingsByUser() {
    }

    @Test
    void getBookingsForAllItemsByUser() {
    }

    @Test
    void getBookingById() {
    }
}