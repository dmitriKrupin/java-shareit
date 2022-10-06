package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoIn {
    private Long id;
    private String name;
    private String description;
    private Boolean available;

    private Long requestId;

    public ItemDtoIn(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
