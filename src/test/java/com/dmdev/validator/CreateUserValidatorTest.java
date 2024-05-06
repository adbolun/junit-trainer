package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void shouldPassValidation() {
        //given - a valid DTO
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        //when
        ValidationResult actualResult = validator.validate(createUserDto);

        //then
        Assertions.assertTrue(actualResult.isValid());
    }

    @Test
    void invalidBirthday() {
        //given - an invalid DTO
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01 12:23")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        //when
        ValidationResult actualResult = validator.validate(createUserDto);

        //then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void invalidGender() {
        //given - an invalid DTO
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender("fake")
                .build();

        //when
        ValidationResult actualResult = validator.validate(createUserDto);

        //then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void invalidRole() {
        //given - an invalid DTO
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role("fake")
                .gender(Gender.MALE.name())
                .build();

        //when
        ValidationResult actualResult = validator.validate(createUserDto);

        //then
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.role");
    }

    @Test
    void invalidBirthdayGenderRole() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01 12:45")
                .role("fake")
                .gender("fake")
                .build();

        ValidationResult actualResult = validator.validate(createUserDto);

        assertThat(actualResult.getErrors()).hasSize(3);

        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(errorCodes).contains(errorCodes.get(0), errorCodes.get(1), errorCodes.get(2));
    }
}