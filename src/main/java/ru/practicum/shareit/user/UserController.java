package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsersList() {
        log.info("Получаем GET запрос к эндпойнту /users");
        List<User> usersList = userService.getAllUsersList();
        return UserMapper.toUsersListDto(usersList);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.info("Получаем GET запрос к эндпойнту /users/{}", userId);
        User user = userService.findUserById(userId);
        return UserMapper.toUserDto(user);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Получаем POST запрос к эндпойнту /users");
        User user = userService.addUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto updateUserDto) {
        log.info("Получаем PATCH запрос к эндпойнту /users/{}", userId);
        updateUserDto.setId(userId);
        User user = userService.updateUser(UserMapper.toUser(updateUserDto));
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получаем DELETE запрос к эндпойнту /users/{}", userId);
        User userForDelete = userService.findUserById(userId);
        userService.deleteUser(userForDelete);
    }

}
