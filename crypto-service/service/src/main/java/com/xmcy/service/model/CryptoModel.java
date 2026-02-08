package com.xmcy.service.model;

import java.time.LocalDateTime;

/**
 * @param localDateTime the data was recorded
 * @param symbol        identifying crypto type
 * @param price         in usd
 */
public record CryptoModel(LocalDateTime localDateTime, String symbol, double price) {
}
