package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void toDTO() {
        User user = new User();
        user.setId(1);
        user.setName("lecture name");

        UserDto userDto = userMapper.toDTO(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());

    }

    @Test
    void toModel() {
        UserDto userDto = new UserDto();
        userDto.setId(2);
        userDto.setEmail("test@mail.com");

        User user = userMapper.toModel(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
    }
}