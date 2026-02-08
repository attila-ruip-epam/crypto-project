package com.xmcy.controller.mapper;

import com.xmcy.model.ApiCryptoRange;
import com.xmcy.service.model.CryptoAndNormalizedRange;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CryptoAndNormalizedRangeMapperTest {

    @Test
    void convertShouldBeCorrect() {
        var input = new CryptoAndNormalizedRange("BTC", 1.0);

        ApiCryptoRange actual = CryptoAndNormalizedRangeMapper.INSTANCE.convert(input);

        assertThat(actual).isEqualTo(new ApiCryptoRange().symbol("BTC").normalizedRange(1.0));
    }

    @Test
    void convertShouldBeCorrectWithNullInput() {
        ApiCryptoRange actual = CryptoAndNormalizedRangeMapper.INSTANCE.convert(null);

        assertThat(actual).isNull();
    }
}
