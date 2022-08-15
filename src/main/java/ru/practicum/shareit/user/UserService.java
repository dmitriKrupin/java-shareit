package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    List<User> getAllUsersList();

    User findUserById(long userId);
}
