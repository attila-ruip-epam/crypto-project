package com.xmcy.service.model;

/**
 * @param oldest model by date
 * @param newest model by date
 * @param min    model by price
 * @param max    model by price
 */
public record CryptoLimits(CryptoModel oldest, CryptoModel newest, CryptoModel min, CryptoModel max) {
}
