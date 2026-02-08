package com.xmcy.controller;

import com.xmcy.model.ApiCrypto;
import com.xmcy.model.ApiCryptoLimits;
import com.xmcy.model.ApiCryptoRange;
import com.xmcy.service.CryptoRecommendationService;
import com.xmcy.service.model.CryptoAndNormalizedRange;
import com.xmcy.service.model.CryptoLimits;
import com.xmcy.service.model.CryptoModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoControllerTest {

    @Mock
    private CryptoRecommendationService cryptoService;

    @InjectMocks
    private CryptoController cryptoController;

    @Test
    void cryptoHighGetShouldBeCorrect() {
        LocalDate localDate = LocalDate.now();
        var cryptoAndNormalizedRange = new CryptoAndNormalizedRange("BTC", 1.0);
        when(cryptoService.getHighestNormalizedCryptorForDay(localDate)).thenReturn(cryptoAndNormalizedRange);

        ResponseEntity<ApiCryptoRange> actual = cryptoController.cryptoHighGet(localDate);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody().getSymbol()).isEqualTo("BTC");
        assertThat(actual.getBody().getNormalizedRange()).isEqualTo(1.0);
    }

    @Test
    void cryptoLimitsGetShouldBeCorrect() {
        String symbol = "BTC";
        LocalDateTime now = LocalDateTime.now();
        CryptoModel oldest = new CryptoModel(now.minusDays(10), symbol, 2.0);
        CryptoModel newest = new CryptoModel(now.plusDays(10), symbol, 4.0);
        CryptoModel min = new CryptoModel(now.minusDays(3), symbol, 1.0);
        CryptoModel max = new CryptoModel(now.plusDays(2), symbol, 5.0);
        CryptoLimits cryptoLimits = new CryptoLimits(oldest, newest, min, max);
        when(cryptoService.getCryptoLimits(symbol)).thenReturn(cryptoLimits);

        ResponseEntity<ApiCryptoLimits> actual = cryptoController.cryptoLimitsGet(symbol);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isNotNull();

        assertThat(actual.getBody().getOldest()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate().minusDays(10))
            .symbol(symbol)
            .price(2.0));

        assertThat(actual.getBody().getNewest()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate().plusDays(10))
            .symbol(symbol)
            .price(4.0));

        assertThat(actual.getBody().getMin()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate().minusDays(3))
            .symbol(symbol)
            .price(1.0));

        assertThat(actual.getBody().getMax()).isEqualTo(new ApiCrypto()
            .date(now.toLocalDate().plusDays(2))
            .symbol(symbol)
            .price(5.0));
    }
}
