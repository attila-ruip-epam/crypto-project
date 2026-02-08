package com.xmcy.controller;

import com.xmcy.model.ApiCryptoRange;
import com.xmcy.service.CryptoRecommendationService;
import com.xmcy.service.model.CryptoAndNormalizedRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptosControllerTest {

    @Mock
    private CryptoRecommendationService cryptoService;

    @InjectMocks
    private CryptosController cryptoController;

    @Test
    void cryptosGetShouldNotFailWithEmptyList() {
        when(cryptoService.getCryptoToNormalizedRange()).thenReturn(List.of());

        ResponseEntity<List<ApiCryptoRange>> actual = cryptoController.cryptosGet();

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEmpty();
    }

    @Test
    void cryptosGetShouldBeCorrectForNotEmptyList() {
        List<CryptoAndNormalizedRange> cryptos = new ArrayList<>();
        cryptos.add(new CryptoAndNormalizedRange("BTC", 1.0));
        cryptos.add(new CryptoAndNormalizedRange("ETH", 2.0));
        when(cryptoService.getCryptoToNormalizedRange()).thenReturn(cryptos);

        ResponseEntity<List<ApiCryptoRange>> actual = cryptoController.cryptosGet();

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).hasSize(2);
        assertThat(actual.getBody().getFirst()).isEqualTo(new ApiCryptoRange().symbol("BTC").normalizedRange(1.0));
        assertThat(actual.getBody().getLast()).isEqualTo(new ApiCryptoRange().symbol("ETH").normalizedRange(2.0));
    }
}
