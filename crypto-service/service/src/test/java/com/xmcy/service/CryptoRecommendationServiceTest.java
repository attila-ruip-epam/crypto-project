package com.xmcy.service;

import com.xmcy.service.exception.CryptoNotFoundException;
import com.xmcy.service.model.CryptoAndNormalizedRange;
import com.xmcy.service.model.CryptoLimits;
import com.xmcy.service.model.CryptoModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoRecommendationServiceTest {

    @Mock
    private CryptoProvider cryptoProvider;

    @InjectMocks
    private CryptoRecommendationService cryptoService;

    @Test
    void getCryptoToNormalizedRangeShouldBeCorrect() {
        Map<String, List<CryptoModel>> data = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        data.put("BTC", List.of(new CryptoModel(now, "BTC", 3),
            new CryptoModel(now, "BTC", 2),
            new CryptoModel(now, "BTC", 2)));
        data.put("LTC", List.of(new CryptoModel(now, "LTC", 3),
            new CryptoModel(now, "LTC", 2),
            new CryptoModel(now, "LTC", 2)));
        data.put("ETH", List.of(new CryptoModel(now, "ETH", 20),
            new CryptoModel(now, "ETH", 40),
            new CryptoModel(now, "ETH", 60)));
        data.put("DOGE", List.of(new CryptoModel(now, "DOGE", 2),
            new CryptoModel(now, "DOGE", 4),
            new CryptoModel(now, "DOGE", 5)));

        when(cryptoProvider.getCryptoToModels()).thenReturn(Map.copyOf(data));

        var actual = cryptoService.getCryptoToNormalizedRange();

        assertThat(actual).containsExactly(
            new CryptoAndNormalizedRange("ETH", 2),
            new CryptoAndNormalizedRange("DOGE", 1.5),
            new CryptoAndNormalizedRange("BTC", 0.5),
            new CryptoAndNormalizedRange("LTC", 0.5));
    }

    @Test
    void getCryptoLimitsShouldBeCorrect() {
        String symbol = "DOGE";
        CryptoModel expectedOldest = new CryptoModel(LocalDateTime.now().minusDays(100), symbol, 20);
        CryptoModel expectedNewest = new CryptoModel(LocalDateTime.now().plusDays(100), symbol, 22);
        CryptoModel expectedMin = new CryptoModel(LocalDateTime.now(), symbol, 10);
        CryptoModel expectedMax = new CryptoModel(LocalDateTime.now(), symbol, 25);
        CryptoModel other = new CryptoModel(LocalDateTime.now().plusMinutes(1), symbol, 10);
        CryptoModel other2 = new CryptoModel(LocalDateTime.now(), symbol, 21);

        when(cryptoProvider.getCryptoModels(symbol))
            .thenReturn(List.of(other, expectedNewest, expectedMax, expectedMin, expectedOldest, other2));

        CryptoLimits actual = cryptoService.getCryptoLimits(symbol);

        assertThat(actual.oldest()).isEqualTo(expectedOldest);
        assertThat(actual.newest()).isEqualTo(expectedNewest);
        assertThat(actual.min()).isEqualTo(expectedMin);
        assertThat(actual.max()).isEqualTo(expectedMax);
    }

    @Test
    void getCryptoLimitsShouldThrowException() {
        String symbol = "DOGE";

        when(cryptoProvider.getCryptoModels(symbol)).thenReturn(List.of());

        assertThatThrownBy(() -> cryptoService.getCryptoLimits(symbol))
            .isInstanceOf(CryptoNotFoundException.class);
    }

    @Test
    void getHighestNormalizedCryptorForDayShouldBeCorrect() {
        LocalDate localDate = LocalDate.now().plusDays(1);

        Map<String, List<CryptoModel>> data = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        data.put("BTC", List.of(new CryptoModel(now, "BTC", 3),
            new CryptoModel(now, "BTC", 2),
            new CryptoModel(now, "BTC", 2)));
        data.put("LTC", List.of(new CryptoModel(now, "LTC", 3),
            new CryptoModel(now, "LTC", 2),
            new CryptoModel(now, "LTC", 2)));
        data.put("ETH", List.of(new CryptoModel(now, "ETH", 20),
            new CryptoModel(now, "ETH", 40),
            new CryptoModel(now, "ETH", 60)));
        data.put("DOGE", List.of(new CryptoModel(now.plusDays(1), "DOGE", 2),
            new CryptoModel(now.plusDays(1), "DOGE", 4),
            new CryptoModel(now.plusDays(1), "DOGE", 5)));

        when(cryptoProvider.getCryptoModelsStream()).thenReturn(data.values().stream().flatMap(Collection::stream));

        var actual = cryptoService.getHighestNormalizedCryptorForDay(localDate);

        assertThat(actual.symbol()).isEqualTo("DOGE");
        assertThat(actual.normalizedRange()).isEqualTo(1.5);
    }
}
