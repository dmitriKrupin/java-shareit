package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    List<User> getAllUsersList();

    User findUserById(long userId);
}
