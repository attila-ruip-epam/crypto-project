package com.xmcy.service;

import com.xmcy.service.model.CryptoModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Interface for crypto service calculations. Data can come from various sources.
 */
public interface CryptoProvider {

    /**
     * @return the available crypto models as a stream
     */
    Stream<CryptoModel> getCryptoModelsStream();

    /**
     * @param symbol identifying the crypto (eg: BTC, ETH, DOGE), no enum used for extendability without new app version
     * @return the available crypto models
     */
    List<CryptoModel> getCryptoModels(String symbol);

    /**
     * @return a map with pf all crypto models grouped by symbol
     */
    Map<String, List<CryptoModel>> getCryptoToModels();

}
