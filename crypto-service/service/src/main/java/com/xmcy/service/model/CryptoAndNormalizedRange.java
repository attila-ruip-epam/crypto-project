package com.xmcy.service.model;

/**
 * @param symbol          identifying crypto type
 * @param normalizedRange calculated from prices
 */
public record CryptoAndNormalizedRange(String symbol, double normalizedRange) {
}
