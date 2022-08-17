package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsersList();

    User findUserById(long userId);

    User addUser(User user);

    User updateUser(User updateUser);

    void deleteUser(User userForDelete);
}
