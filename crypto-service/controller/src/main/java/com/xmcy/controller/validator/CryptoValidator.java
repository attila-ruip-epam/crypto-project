package com.xmcy.controller.validator;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Validates crypto input based on configured, supported cryptos
 */
public class CryptoValidator {

    private final Set<String> supportedCryptos;

    public CryptoValidator(Set<String> supportedCryptos) {
        this.supportedCryptos = supportedCryptos;
    }

    /**
     * @param symbol identifying crypto to validate
     */
    public void validate(String symbol) {
        if (StringUtils.isBlank(symbol) || !supportedCryptos.contains(symbol)) {
            throw new ValidationException();
        }
    }
}
