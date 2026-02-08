package com.xmcy.service.config;

import com.xmcy.service.CryptoProvider;
import com.xmcy.service.CryptoRecommendationService;
import com.xmcy.service.cache.CsvLoadingCryptoCache;
import com.xmcy.service.csv.CsvFileReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean//(name = "csvLoadingCryptoCache")
    public CryptoProvider csvLoadingCryptoCache(CsvFileReader csvFileReader) {
        return new CsvLoadingCryptoCache(csvFileReader);
    }

    @Bean
    public CsvFileReader csvFileReader() {
        return new CsvFileReader();
    }

    @Bean
    public CryptoRecommendationService cryptoService(CryptoProvider cryptoProvider) {
        return new CryptoRecommendationService(cryptoProvider);
    }
}
