package com.xmcy.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.xmcy.service.exception.CryptoServiceException;
import com.xmcy.service.model.CryptoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Reads cvs files from classpath and converts data to crypto models.
 */
@Slf4j
public class CsvFileReader {
    private static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

    /**
     * @return the list of loaded {@link CryptoModel}s
     * @throws CryptoServiceException if io exception
     */
    public List<CryptoModel> loadCsvs() throws CryptoServiceException {
        try {
            Resource[] resources = RESOLVER.getResources("classpath:cryptodata/*.csv");
            return Arrays.stream(resources)
                .map(this::loadCsv)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .toList();
        } catch (IOException e) {
            log.error("Failed to load csv files due to", e);
            throw new CryptoServiceException(e);
        }
    }

    private List<CryptoModel> loadCsv(Resource resource) {
        List<CryptoModel> records;
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            records = csvReader.readAll()
                .stream()
                .map(CsvLineConverter::convert)
                .toList();
        } catch (IOException | CsvException e) {
            log.error("Failed to load csv file {}", resource.getFilename(), e);
            records = List.of();
        }
        return records;
    }
}
