package com.xmcy.service.cache;

import com.xmcy.service.CryptoProvider;
import com.xmcy.service.csv.CsvFileReader;
import com.xmcy.service.model.CryptoModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
public class CsvLoadingCryptoCache implements CryptoProvider {

    private final CsvFileReader csvFileReader;
    private Map<String, List<CryptoModel>> cryptoToModels;

    @EventListener
    public void loadCache(ApplicationReadyEvent event) {
        loadCacheIfNeeded();
    }

    @Override
    public Stream<CryptoModel> getCryptoModelsStream() {
        loadCacheIfNeeded();
        return cryptoToModels.values().stream().flatMap(Collection::stream);
    }

    @Override
    public List<CryptoModel> getCryptoModels(String symbol) {
        return getCryptoToModels().get(symbol);
    }

    @Override
    public Map<String, List<CryptoModel>> getCryptoToModels() {
        loadCacheIfNeeded();
        return cryptoToModels;
    }

    private void loadCache() {
        log.info("Loading crypto cache from csv");
        Map<String, List<CryptoModel>> models = csvFileReader.loadCsvs()
            .stream()
            .collect(groupingBy(CryptoModel::symbol));
        cryptoToModels = Map.copyOf(models);// immutable
        log.info("Loaded {} cryptos", cryptoToModels.keySet());
    }

    private void loadCacheIfNeeded() {
        if (cryptoToModels == null) {
            loadCache();
        }
    }
}
