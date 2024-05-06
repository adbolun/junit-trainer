package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class UserServiceIT extends IntegrationTestBase {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void init() {
        userDao = UserDao.getInstance();
        userService = new UserService(
                CreateUserValidator.getInstance(),
                userDao,
                CreateUserMapper.getInstance(),
                UserMapper.getInstance()
        );
    }

    @Test
    void login() {
        User user = userDao.save(getUser("test@gmail.com"));

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void create() {
        CreateUserDto createUserDto = getCreateUserDto();

        UserDto actualResult = userService.create(createUserDto);

        assertThat(actualResult.getId());
    }

    private User getUser(String email) {
        return User.builder()
                .name("Ivan")
                .email(email)
                .password("123")
                .birthday(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }

    private CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .email("ivan@gmail.com")
                .password("123")
                .birthday("2020-01-01")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .name("Ivan")
                .email("ivan@gmail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }
}
