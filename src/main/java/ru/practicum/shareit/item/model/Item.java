package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    //В пакете item создайте класс Item и добавьте в него следующие поля:
    //id — уникальный идентификатор вещи;
    //name — краткое название;
    //description — развёрнутое описание;
    //available — статус о том, доступна или нет вещь для аренды;
    //owner — владелец вещи;
    //request — если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос.
}
