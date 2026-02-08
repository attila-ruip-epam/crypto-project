package com.xmcy.service;

import com.xmcy.service.exception.CryptoNotFoundException;
import com.xmcy.service.model.CryptoAndNormalizedRange;
import com.xmcy.service.model.CryptoLimits;
import com.xmcy.service.model.CryptoModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;

@RequiredArgsConstructor
public class CryptoRecommendationService implements CryptoService {

    private final CryptoProvider cryptoProvider;

    @Override
    public List<CryptoAndNormalizedRange> getCryptoToNormalizedRange() {
        return cryptoProvider.getCryptoToModels()
            .entrySet()
            .stream()
            .map(e -> new CryptoAndNormalizedRange(e.getKey(), getNormalizedRange(e.getValue())))
            .sorted(comparingDouble(CryptoAndNormalizedRange::normalizedRange)
                .reversed()
                .thenComparing(CryptoAndNormalizedRange::symbol)) // to have secondary sorting if eq by first order
            .toList();
    }

    @Override
    public CryptoLimits getCryptoLimits(String symbol) {
        var cryptoModels = cryptoProvider.getCryptoModels(symbol);
        if (CollectionUtils.isEmpty(cryptoModels)) {
            throw new CryptoNotFoundException();
        }
        var oldestNewest = getOldestNewestByDate(cryptoModels);
        var minMax = getMinMaxByPrice(cryptoModels);

        return new CryptoLimits(oldestNewest.getLeft(), oldestNewest.getRight(), minMax.getLeft(), minMax.getRight());
    }


    @Override
    public CryptoAndNormalizedRange getHighestNormalizedCryptorForDay(LocalDate day) {
        List<CryptoAndNormalizedRange> models = cryptoProvider.getCryptoModelsStream()
            .filter(cryptoModel -> cryptoModel.localDateTime().toLocalDate().equals(day))
            .collect(Collectors.groupingBy(CryptoModel::symbol))
            .entrySet()
            .stream()
            .map(e -> new CryptoAndNormalizedRange(e.getKey(), getNormalizedRange(e.getValue())))
            .sorted(comparingDouble(CryptoAndNormalizedRange::normalizedRange))
            .toList();
        if (CollectionUtils.isEmpty(models)) {
            throw new CryptoNotFoundException();
        }
        return models.getLast();
    }

    private double getNormalizedRange(List<CryptoModel> models) {
        var minMax = getMinMaxByPrice(models);
        var min = minMax.getLeft();
        var max = minMax.getRight();

        double normalized = (max.price() - min.price()) / min.price();
        return BigDecimal.valueOf(normalized)
            .setScale(3, RoundingMode.HALF_UP)
            .doubleValue();
    }

    private Pair<CryptoModel, CryptoModel> getOldestNewestByDate(List<CryptoModel> models) {
        var sortedModels = models
            .stream()
            .sorted(comparing(CryptoModel::localDateTime)
                .thenComparing(CryptoModel::price)) // to have secondary sorting if eq by first order
            .toList();

        return Pair.of(sortedModels.getFirst(), sortedModels.getLast());
    }

    private Pair<CryptoModel, CryptoModel> getMinMaxByPrice(List<CryptoModel> models) {
        var sortedModels = models.stream()
            .sorted(comparingDouble(CryptoModel::price)
                .thenComparing(CryptoModel::localDateTime)) // to have secondary sorting if eq by first order
            .toList();

        return Pair.of(sortedModels.getFirst(), sortedModels.getLast());
    }
}
