package com.xmcy.integrationtest;

import com.xmcy.application.Application;
import com.xmcy.service.CryptoRecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class GetCryptosIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private CryptoRecommendationService cryptoService;

    @WithMockUser
    @Test
    void getCryptosShouldReturn200() throws Exception {
        log.info("Running getCryptosShouldReturn200");
        String expectedContent = """
            [{"normalizedRange":0.638,"symbol":"ETH"},{"normalizedRange":0.506,"symbol":"XRP"},{"normalizedRange":0.505,"symbol":"DOGE"},{"normalizedRange":0.465,"symbol":"LTC"},{"normalizedRange":0.434,"symbol":"BTC"}]
            """;
        mockMvc.perform(get("/cryptos")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedContent));
    }

    @Test
    void getCryptosShouldReturn401() throws Exception {
        log.info("Running getCryptosShouldReturn401");
        mockMvc.perform(get("/cryptos")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getCryptosShouldReturn5XX() throws Exception {
        log.info("Running getCryptosShouldReturn5XX");
        when(cryptoService.getCryptoToNormalizedRange()).thenThrow(new RuntimeException());
        String expectedJson = """
            {"message":"Unexpected exception"}
            """;
        mockMvc.perform(get("/cryptos")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void wgetCryptosShouldReturn429AfterTooManyRequests() throws Exception {
        log.info("Running wgetCryptosShouldReturn429AfterTooManyRequests");
        Set<Integer> responses = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            responses.add(mockMvc.perform(get("/cryptos")
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus());
        }

        assertThat(responses).contains(429);
    }
}
