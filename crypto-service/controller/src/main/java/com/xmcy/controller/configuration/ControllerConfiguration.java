package com.xmcy.controller.configuration;

import com.xmcy.controller.validator.CryptoValidator;
import com.xmcy.service.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration
@Import(ServiceConfig.class)
public class ControllerConfiguration {

    @Bean
    public CryptoValidator cryptoValidator(@Value("${xmcy.supported.cryptos}") Set<String> supportedCryptos) {
        return new CryptoValidator(supportedCryptos);
    }

}
