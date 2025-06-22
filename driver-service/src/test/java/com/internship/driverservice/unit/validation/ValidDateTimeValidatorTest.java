package com.internship.driverservice.unit.validation;

import com.internship.driverservice.utils.validation.ValidDateTimeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ValidDateTimeValidatorTest {

    private ValidDateTimeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidDateTimeValidator();
    }

    @Test
    void isValid_returnsTrue_forValidDateTime() {
        boolean result = validator.isValid("2025-04-03T10:00:00", null);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_returnsFalse_forInvalidDateTime() {
        boolean result = validator.isValid("not-a-date-time", null);
        assertThat(result).isFalse();
    }

    @Test
    void isValid_returnsTrue_whenValueIsNull() {
        boolean result = validator.isValid(null, null);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_returnsTrue_whenValueIsEmpty() {
        boolean result = validator.isValid("", null);
        assertThat(result).isTrue();
    }
}