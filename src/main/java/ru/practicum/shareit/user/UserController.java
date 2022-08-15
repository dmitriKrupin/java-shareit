package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UsersListMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsersList() {
        log.info("Получаем GET запрос к эндпойнту /users");
        List<User> usersList = userService.getAllUsersList();
        return UsersListMapper.INSTANCE.toDTOList(usersList);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.info("Получаем GET запрос к эндпойнту /users/{}", userId);
        User user = userService.findUserById(userId);
        return UserMapper.INSTANCE.toDTO(user);
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        log.info("Получаем POST запрос к эндпойнту /users");
        User userToDto = userService.addUser(user);
        return UserMapper.INSTANCE.toDTO(userToDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody User updateUser) {
        log.info("Получаем PATCH запрос к эндпойнту /users/{}", userId);
        updateUser.setId(userId);
        return UserMapper.INSTANCE.toDTO(userService.updateUser(updateUser));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получаем DELETE запрос к эндпойнту /users/{}", userId);
        User userForDelete = userService.findUserById(userId);
        userService.deleteUser(userForDelete);
    }

}
