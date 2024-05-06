package com.dmdev.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

class LocalDateFormatterTest {

    @Test
    void format() {
        //Given
        String date = "2022-11-28";

        //When
        LocalDate actualResult = LocalDateFormatter.format(date);

        //Then
        assertThat(actualResult).isEqualTo(LocalDate.of(2022, 11, 28));
    }

    @Test
    void shouldThrowExceptionIfDateInvalid() {
        String date = "2022-11-28 12:25";

        assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void valid(String date, boolean expectedResult) {
        boolean actualResult = LocalDateFormatter.isValid(date);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2022-11-28", true),
                Arguments.of("01-01-2001", false),
                Arguments.of("01-01-2001 12:25", false),
                Arguments.of(null, false),
                Arguments.of("", false)
        );
    }
}