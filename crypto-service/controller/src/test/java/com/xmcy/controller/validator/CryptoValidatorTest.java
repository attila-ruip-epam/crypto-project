package com.xmcy.controller.validator;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CryptoValidatorTest {

    @Test
    void validateShouldValidateCorrectly() {
        CryptoValidator validator = new CryptoValidator(Set.of("DOGE"));

        assertThatThrownBy(() -> validator.validate("BTC")).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> validator.validate("")).isInstanceOf(ValidationException.class);

        validator.validate("DOGE");
    }
}
