package com.xmcy.controller.mapper;

import com.xmcy.model.ApiCrypto;
import com.xmcy.model.ApiCryptoLimits;
import com.xmcy.service.model.CryptoLimits;
import com.xmcy.service.model.CryptoModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CryptoLimitsMapperTest {

    @Test
    void convertShouldbeCorrect() {
        LocalDateTime now = LocalDateTime.now();
        String symbol = "BTC";
        CryptoModel oldest = new CryptoModel(now.minusDays(1), symbol, 1.5);
        CryptoModel newest = new CryptoModel(now.plusDays(1), symbol, 2.0);
        CryptoModel min = new CryptoModel(now, symbol, 1.0);
        CryptoModel max = new CryptoModel(now, symbol, 4.0);
        CryptoLimits input = new CryptoLimits(oldest, newest, min, max);

        ApiCryptoLimits actual = CryptoLimitsMapper.INSTANCE.convert(input);

        assertThat(actual.getOldest()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate().minusDays(1))
            .symbol(symbol)
            .price(1.5));

        assertThat(actual.getNewest()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate().plusDays(1))
            .symbol(symbol)
            .price(2.0));

        assertThat(actual.getMin()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate())
            .symbol(symbol)
            .price(1.0));

        assertThat(actual.getMax()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate())
            .symbol(symbol)
            .price(4.0));
    }

    @Test
    void convertShouldbeCorrectWithNullInput() {
        ApiCryptoLimits actual = CryptoLimitsMapper.INSTANCE.convert(null);

        assertThat(actual).isNull();
    }

    @Test
    void convertShouldbeCorrectWithNullLimits() {
        CryptoLimits input = new CryptoLimits(null, null, null, null);

        ApiCryptoLimits actual = CryptoLimitsMapper.INSTANCE.convert(input);

        assertThat(actual.getOldest()).isNull();
        assertThat(actual.getNewest()).isNull();
        assertThat(actual.getMin()).isNull();
        assertThat(actual.getMax()).isNull();
    }
}
