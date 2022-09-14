package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest { //И в пакете request будет ItemRequest — класс, отвечающий за запрос вещи.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; //id — уникальный идентификатор запроса;
    @Column(name = "description")
    private String description; //description — текст запроса, содержащий описание требуемой вещи;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor; //requestor — пользователь, создавший запрос;
    @Column(name = "created")
    private LocalDate created; //created — дата и время создания запроса.
}
