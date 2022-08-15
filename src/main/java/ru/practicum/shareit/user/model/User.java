package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;

@Data
@EqualsAndHashCode
public class User {
    private long id;
    @Email
    private String email;
    private String name;
}
