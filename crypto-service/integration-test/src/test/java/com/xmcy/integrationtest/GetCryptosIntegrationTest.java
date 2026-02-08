package com.xmcy.integrationtest;

import com.xmcy.application.Application;
import com.xmcy.service.CryptoRecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GetCryptosIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private CryptoRecommendationService cryptoService;

    @WithMockUser
    @Test
    void getCryptosShouldReturn200() throws Exception {
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
        mockMvc.perform(get("/cryptos")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getCryptosShouldReturn5XX() throws Exception {
        when(cryptoService.getCryptoToNormalizedRange()).thenThrow(new RuntimeException());
        String expectedJson = """
            {"message":"Unexpected exception"}
            """;
        mockMvc.perform(get("/cryptos")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedJson));
    }
}
