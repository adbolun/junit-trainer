package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private CreateUserValidator createUserValidator;

    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess() {
        //We stub the User entity
        User user = getUser();

        //We stub the UserDto
        UserDto expectedResult = getUserDto();

        //We don't have the login and the password, there we need to create a STUB object
        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(user.getEmail(), user.getPassword());
        doReturn(expectedResult).when(userMapper).map(user);

        //To receive UserDto we need to make another STUB object on the mapper
        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(expectedResult);
    }

    @Test
    void loginFailed() {
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(anyString(), anyString());

        Optional<UserDto> actualResult = userService.login("dummy", "dummy");

        assertThat(actualResult).isEmpty();

        //We should to check if userMapper mock was not used
        verifyNoInteractions(userMapper);
    }

    @Test
    void create() {
        User user = getUser();
        CreateUserDto createUserDto = getCreateUserDto();
        UserDto expectedResult = getUserDto();

        //We need stubbing the other dependency in UserService create method
        doReturn(new ValidationResult()).when(createUserValidator).validate(createUserDto);
        doReturn(user).when(createUserMapper).map(createUserDto);
        doReturn(expectedResult).when(userMapper).map(user);

        UserDto actualResult = userService.create(createUserDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(userDao).save(user);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalid() {
        CreateUserDto createUserDto = getCreateUserDto();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.role", "message"));

        doReturn(validationResult).when(createUserValidator).validate(createUserDto);

        assertThrows(ValidationException.class, () -> userService.create(createUserDto));
        verifyNoInteractions(userDao, createUserMapper, userMapper);
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

    private User getUser() {
        return User.builder()
                .id(1)
                .name("Ivan")
                .email("ivan@gmail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .name("Ivan")
                .email("ivan@gmail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }
}