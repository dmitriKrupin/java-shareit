package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsersList() {
        log.info("Получаем GET запрос к эндпойнту /users");
        return userService.getAllUsersDtoList();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.info("Получаем GET запрос к эндпойнту /users/{}", userId);
        return userService.findUserById(userId);
    }

    @PostMapping
    public UserDto createUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("Получаем POST запрос к эндпойнту /users");
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Validated({Update.class}) @PathVariable long userId, @RequestBody UserDto updateUserDto) {
        log.info("Получаем PATCH запрос к эндпойнту /users/{}", userId);
        updateUserDto.setId(userId);
        return userService.updateUser(updateUserDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получаем DELETE запрос к эндпойнту /users/{}", userId);
        userService.deleteUserDto(userId);
    }

}