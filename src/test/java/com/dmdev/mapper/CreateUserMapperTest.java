package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void map() {
        //given
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        //when
        User actualResult = mapper.map(createUserDto);

        User expectedResult = User.builder()
                .name(actualResult.getName())
                .email(actualResult.getEmail())
                .password(actualResult.getPassword())
                .birthday(actualResult.getBirthday())
                .role(actualResult.getRole())
                .gender(actualResult.getGender())
                .build();

        //then
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}