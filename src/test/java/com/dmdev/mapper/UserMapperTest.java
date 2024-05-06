package com.dmdev.mapper;

import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class UserMapperTest {

    private final UserMapper mapper = UserMapper.getInstance();

    @Test
    void map() {
        User user = User.builder()
                .id(1)
                .name("Ivan")
                .email("ivan@gmail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();

        UserDto actualResult = mapper.map(user);

        UserDto expectedResult = UserDto.builder()
                .id(actualResult.getId())
                .name(actualResult.getName())
                .email(actualResult.getEmail())
                .birthday(user.getBirthday())
                .role(user.getRole())
                .gender(user.getGender())
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}