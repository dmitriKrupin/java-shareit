package ru.practicum.shareit.user.daoImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ExceptionController;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component ("InMemoryUserDaoImpl")
@Slf4j
public class InMemoryUserDaoImpl implements UserDao {
    private final List<User> usersList = new ArrayList<>();
    private long userIdCounter = 0;

    private final ExceptionController exceptionController;

    public InMemoryUserDaoImpl(ExceptionController exceptionController) {
        this.exceptionController = exceptionController;
    }

    @Override
    public User addUser(User user) {
        if (isValidate(user)) {
            userIdCounter++;
            user.setId(userIdCounter);
            usersList.add(user);
        }
        return user;
    }

    @Override
    public User updateUser(User updateUser) {
        User oldUser = findUserById(updateUser.getId());
        if (updateUser.getName() != null) {
            oldUser.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            if (isValidate(updateUser)) {
                oldUser.setEmail(updateUser.getEmail());
            }
        }
        return oldUser;
    }

    @Override
    public void deleteUser(User userForDelete) {
        usersList.remove(userForDelete);
    }

    @Override
    public List<User> getAllUsersList() {
        return usersList;
    }

    @Override
    public User findUserById(long userId) {
        User user = new User();
        for (User entry : usersList) {
            if (entry.getId() == userId) {
                user = entry;
            }
        }
        return user;
    }

    private boolean isValidate(User userForValidate) {
        boolean isValidate = true;
        if (userForValidate.getEmail() == null) {
            log.error("Адрес пользователя {} не должен быть пустым!", userForValidate.getName());
            throw exceptionController.badRequestException(new BadRequestException("Адрес не должен быть пустым!"));
        }
        if (!userForValidate.getEmail().contains("@")) {
            log.error("Адрес пользователя {} не содержит символ @", userForValidate.getName());
            throw exceptionController.badRequestException(new BadRequestException("Адрес не должен быть пустым!"));
        }
        for (User entry : usersList) {
            if (Objects.equals(userForValidate.getEmail(), entry.getEmail())) {
                isValidate = false;
                log.error("Адрес {} уже есть в базе!", userForValidate.getEmail());
                throw exceptionController.conflictException(new ConflictException("Такой адрес уже есть в базе!"));
            }
        }
        return isValidate;
    }
}