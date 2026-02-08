package com.xmcy.service;

import com.xmcy.service.model.CryptoAndNormalizedRange;
import com.xmcy.service.model.CryptoLimits;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for crypto services.
 */
public interface CryptoService {
    /**
     * @return descending sorted list of all the cryptos, comparing the normalized range
     */
    List<CryptoAndNormalizedRange> getCryptoToNormalizedRange();

    /**
     * @param symbol identifying the crypto
     * @return the oldest/newest/min/max values for a requested crypto
     */
    CryptoLimits getCryptoLimits(String symbol);

    /**
     * @param day identifying the day
     * @return the crypto with the highest normalized range for specific day
     */
    CryptoAndNormalizedRange getHighestNormalizedCryptorForDay(LocalDate day);
}
