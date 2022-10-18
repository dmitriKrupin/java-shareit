package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDtoOut {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

    public ItemDtoOut(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
