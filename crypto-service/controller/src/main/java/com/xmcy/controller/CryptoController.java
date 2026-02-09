package com.xmcy.controller;

import com.xmcy.api.CryptoApi;
import com.xmcy.controller.mapper.CryptoAndNormalizedRangeMapper;
import com.xmcy.controller.mapper.CryptoLimitsMapper;
import com.xmcy.controller.validator.CryptoValidator;
import com.xmcy.model.ApiCryptoLimits;
import com.xmcy.model.ApiCryptoRange;
import com.xmcy.service.CryptoRecommendationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CryptoController implements CryptoApi {

    private final CryptoRecommendationService cryptoService;
    private final CryptoValidator cryptoValidator;

    @Autowired
    public CryptoController(CryptoRecommendationService cryptoService,
                            CryptoValidator cryptoValidator) {
        this.cryptoService = cryptoService;
        this.cryptoValidator = cryptoValidator;
    }

    @GetMapping(
        value = CryptoApi.PATH_CRYPTO_HIGH_GET,
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiCryptoRange> cryptoHighGet(
        @NotNull @Parameter(name = "date", required = true, in = ParameterIn.QUERY)
        @Valid @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var cryptoAndNormalizedRange = cryptoService.getHighestNormalizedCryptorForDay(date);
        return ResponseEntity.ok(CryptoAndNormalizedRangeMapper.INSTANCE.convert(cryptoAndNormalizedRange));
    }

    @GetMapping(
        value = CryptoApi.PATH_CRYPTO_LIMITS_GET,
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiCryptoLimits> cryptoLimitsGet(
        @NotNull @Parameter(name = "symbol", required = true, in = ParameterIn.QUERY)
        @Valid @RequestParam(value = "symbol") String symbol) {
        cryptoValidator.validate(symbol);
        var cryptoLimits = cryptoService.getCryptoLimits(symbol);
        return ResponseEntity.ok(CryptoLimitsMapper.INSTANCE.convert(cryptoLimits));
    }
}
