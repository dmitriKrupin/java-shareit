package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    private ItemDtoOut itemDtoOut;
    private ItemDtoIn itemDtoIn;

    @BeforeEach
    void beforeEach() {
        itemDtoOut = new ItemDtoOut(1L, "name", "description", true, 2L);
        itemDtoIn = new ItemDtoIn(1L, "name", "description", true);
    }

    @Test
    void getAllItemByUserId() throws Exception {
        when(itemService.findAllItemDtoByUserId(1))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, Mockito.times(1))
                .findAllItemDtoByUserId(1);
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(any(ItemDtoIn.class), any()))
                .thenReturn(itemDtoOut);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoOut)));

        verify(itemService, Mockito.times(1))
                .addItem(itemDtoIn, 1L);
    }

    @Test
    void addComment() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItemById() {
    }

    @Test
    void getItemsBySearch() {
    }
}