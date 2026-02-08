package com.xmcy.service.csv;

import com.xmcy.service.model.CryptoModel;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Converts cvs lines to crypto model.
 */
@Slf4j
final class CsvLineConverter {

    private CsvLineConverter() {
    }

    /**
     * @param values string array containing the csv line values
     * @return the lince converted to model
     */
    public static CryptoModel convert(String[] values) {
        try {
            LocalDateTime localDateTime = convertMillisToLocalDateTime(values[0]);
            String symbol = convertSymbol(values[1]);
            double price = Double.parseDouble(values[2]);
            return new CryptoModel(localDateTime, symbol, price);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            log.warn("Failed to convert {}", values, e);
            return null;
        }
    }

    private static LocalDateTime convertMillisToLocalDateTime(String timestampString) {
        long timestamp = Long.parseLong(timestampString);
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    private static String convertSymbol(String symbol) {
        if (isBlank(symbol)) {
            throw new IllegalArgumentException("Symbol must not be blank");
        }
        return symbol;
    }
}
