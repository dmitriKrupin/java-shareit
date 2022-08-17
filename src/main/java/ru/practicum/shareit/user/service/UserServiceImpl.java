package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsersList() {
        return userDao.getAllUsersList();
    }

    public User findUserById(long userId) {
        return userDao.findUserById(userId);
    }

    public User addUser(User user) {
        return userDao.addUser(user);
    }

    public User updateUser(User updateUser) {
        return userDao.updateUser(updateUser);
    }

    public void deleteUser(User userForDelete) {
        userDao.deleteUser(userForDelete);
    }
}
