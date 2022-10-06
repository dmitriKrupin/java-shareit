package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    private ItemRequestDtoIn itemRequestDtoIn;
    private ItemRequestDtoOut itemRequestDtoOut;
    private ItemRequestDtoOut otherItemRequestDtoOut;

    @BeforeEach
    void beforeEach() {
        itemRequestDtoIn = new ItemRequestDtoIn("request description",
                LocalDateTime.now());
        itemRequestDtoOut = new ItemRequestDtoOut(1L, "request description",
                LocalDateTime.now());
        otherItemRequestDtoOut = new ItemRequestDtoOut(1L, "request description",
                LocalDateTime.now());
    }

    @Test
    void addRequest() throws Exception {
        when(itemRequestService.addRequest(itemRequestDtoIn, 1L))
                .thenReturn(itemRequestDtoOut);
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtoOut)));
        verify(itemRequestService, Mockito.times(1))
                .addRequest(itemRequestDtoIn, 1L);
    }

    @Test
    void getAllRequestsByUserId() throws Exception {
        when(itemRequestService.getAllRequestsByUserId(1L,
                PageRequest.of(0, 10)))
                .thenReturn(List.of(itemRequestDtoOut));
        mockMvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(
                                List.of(itemRequestDtoIn)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        List.of(itemRequestDtoOut))));
        verify(itemRequestService, Mockito.times(1))
                .getAllRequestsByUserId(1L, PageRequest.of(0, 10));
    }

    @Test
    void getAllRequestsOtherUsers() throws Exception {
        when(itemRequestService.getAllRequestsOtherUsers(1L,
                PageRequest.of(0, 10)))
                .thenReturn(List.of(otherItemRequestDtoOut));
        mockMvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(
                                List.of(itemRequestDtoIn)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        List.of(otherItemRequestDtoOut))));
        verify(itemRequestService, Mockito.times(1))
                .getAllRequestsOtherUsers(1L, PageRequest.of(0, 10));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(1L, 1L))
                .thenReturn(itemRequestDtoOut);
        mockMvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtoOut)));
        verify(itemRequestService, Mockito.times(1))
                .getRequestById(1L, 1L);
    }
}