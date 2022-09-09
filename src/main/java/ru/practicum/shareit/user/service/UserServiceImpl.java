package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsersDtoList() {
        List<User> allUsersList = userRepository.findAll();
        return UserMapper.toUsersDtoList(allUsersList);
    }

    public UserDto findUserById(long userDtoId) {
        User user = userRepository.findById(userDtoId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя c id " + userDtoId + " нет"));
        return UserMapper.toUserDto(user);
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(UserDto updateUser) {
        User userFromDto = UserMapper.toUser(updateUser);
        User user = userRepository.findById(userFromDto.getId())
                .orElseThrow(() -> new RuntimeException("Такого пользователя c id " + userFromDto.getId() + " нет"));
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public void deleteUserDto(Long userDtoId) {
        User user = userRepository.findById(userDtoId)
                .orElseThrow(() -> new RuntimeException("Такого пользователя c id " + userDtoId + " нет"));
        userRepository.delete(user);
    }
}
