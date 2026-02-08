package com.xmcy.service.csv;

import com.xmcy.service.model.CryptoModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvFileReaderTest {

    private final CsvFileReader csvFileReader = new CsvFileReader();

    @Test
    void loadCsvsShouldBeCorrect() {
        List<CryptoModel> cryptoModels = csvFileReader.loadCsvs();
        assertThat(cryptoModels).hasSize(450);
    }
}
