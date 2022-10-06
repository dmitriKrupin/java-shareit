package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsersList() {
        log.info("Получаем GET запрос к эндпойнту /users");
        return userClient.getAllUsersDtoList();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Получаем GET запрос к эндпойнту /users/{}", userId);
        return userClient.findUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @Validated({Create.class})
            @RequestBody UserDto userDto) {
        log.info("Получаем POST запрос к эндпойнту /users");
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @Validated({Update.class}) @PathVariable long userId,
            @RequestBody UserDto updateUserDto) {
        log.info("Получаем PATCH запрос к эндпойнту /users/{}", userId);
        updateUserDto.setId(userId);
        return userClient.updateUser(updateUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Получаем DELETE запрос к эндпойнту /users/{}", userId);
        return userClient.deleteUserDto(userId);
    }

}