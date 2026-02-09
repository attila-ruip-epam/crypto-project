package com.xmcy.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.xmcy")
public class CryptoApplication {
    
    static void main(String[] args) {
        SpringApplication.run(CryptoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter(
        @Value("${xmcy.ratelimit.per.minute}") int rateLimit) {
        FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitingFilter(rateLimit));
        registration.addUrlPatterns("/cryptos");
        return registration;
    }
}
