package com.xmcy.service.cache;

import com.xmcy.service.csv.CsvFileReader;
import com.xmcy.service.model.CryptoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvLoadingCryptoCacheTest {

    @Mock
    private CsvFileReader csvFileReader;

    @InjectMocks
    private CsvLoadingCryptoCache cryptoCache;

    @BeforeEach
    void beforeEach() {
        List<CryptoModel> models = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        models.add(new CryptoModel(now, "BTC", 3.1));
        models.add(new CryptoModel(now, "ETH", 4.1));
        models.add(new CryptoModel(now, "ETH", 3.1));
        models.add(new CryptoModel(now, "DOGE", 2.1));
        models.add(new CryptoModel(now, "DOGE", 4.1));
        models.add(new CryptoModel(now, "DOGE", 5.3));
        when(csvFileReader.loadCsvs()).thenReturn(models);
    }

    @Test
    void loadCacheShouldLoadCache() {
        cryptoCache.loadCache(null);
        cryptoCache.loadCache(null);

        verify(csvFileReader, times(1)).loadCsvs();
    }

    @Test
    void getCryptoModelsWithParameterShouldReturnList() {
        List<CryptoModel> cryptoModels = cryptoCache.getCryptoModels("DOGE");

        assertThat(cryptoModels).hasSize(3);
        verify(csvFileReader, times(1)).loadCsvs();
    }

    @Test
    void getCryptoModelsShouldReturnGroupedMap() {
        Map<String, List<CryptoModel>> cryptoModels = cryptoCache.getCryptoToModels();

        assertThat(cryptoModels).hasSize(3);
        assertThat(cryptoModels.get("BTC")).hasSize(1);
        assertThat(cryptoModels.get("ETH")).hasSize(2);
        assertThat(cryptoModels.get("DOGE")).hasSize(3);

        verify(csvFileReader, times(1)).loadCsvs();
    }

    @Test
    void getCryptoModelsShouldReturnImmutableMap() {
        Map<String, List<CryptoModel>> cryptoModels = cryptoCache.getCryptoToModels();

        assertThatThrownBy(cryptoModels::clear).isInstanceOf(UnsupportedOperationException.class);

        verify(csvFileReader, times(1)).loadCsvs();
    }

}
