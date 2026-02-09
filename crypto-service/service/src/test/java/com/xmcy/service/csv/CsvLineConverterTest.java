package com.xmcy.service.csv;

import com.xmcy.service.model.CryptoModel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CsvLineConverterTest {

    @ParameterizedTest
    @MethodSource("provideDataForConversionCheck")
    void convertShouldBeCorrect(String[] line, CryptoModel expected) {
        assertThat(CsvLineConverter.convert(line)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataForConversionCheck() {
        return Stream.of(
            of(null, null),
            of(new String[]{}, null),
            of(new String[]{"a", "b"}, null),
            of(new String[]{"4.5", "BTC", "46813.21"}, null),
            of(new String[]{"timestamp", "symbol", "price"}, null),
            of(new String[]{"1641009600001", " ", "46813.21"}, null),
            of(new String[]{"1641009600002", "BTC", ""}, null),
            of(new String[]{"1641009600000", "BTC", "46813.21"},
                new CryptoModel(LocalDateTime.of(2022, 1, 1, 4, 0), "BTC", 46813.21))
        );
    }
}
