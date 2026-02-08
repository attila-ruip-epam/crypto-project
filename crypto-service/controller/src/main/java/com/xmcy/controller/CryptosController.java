package com.xmcy.controller;

import com.xmcy.api.CryptosApi;
import com.xmcy.controller.mapper.CryptoAndNormalizedRangeMapper;
import com.xmcy.model.ApiCryptoRange;
import com.xmcy.service.CryptoRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CryptosController implements CryptosApi {

    private final CryptoRecommendationService cryptoService;

    @Autowired
    public CryptosController(CryptoRecommendationService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping(
        value = CryptosApi.PATH_CRYPTOS_GET,
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<List<ApiCryptoRange>> cryptosGet() {
        var apiList = cryptoService.getCryptoToNormalizedRange()
            .stream()
            .map(CryptoAndNormalizedRangeMapper.INSTANCE::convert)
            .toList();
        return ResponseEntity.ok(apiList);
    }
}
