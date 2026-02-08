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

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GetCryptoHighIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private CryptoRecommendationService cryptoService;

    @WithMockUser
    @Test
    void getCryptoHighShouldReturn404() throws Exception {
        String expectedJson = """
            {"message":"Crypto not found"}
            """;
        mockMvc.perform(get("/crypto/high")
                .param("date", "2026-02-08")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void getCryptoHighShouldReturn400() throws Exception {
        String expectedJson = """
            {"message":"Invalid input"}
            """;
        mockMvc.perform(get("/crypto/high")
                .param("date", "20260000008")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void getCryptoHighShouldReturn200() throws Exception {
        String expectedJson = """
            {"normalizedRange":0.096,"symbol":"LTC"}
            """;
        mockMvc.perform(get("/crypto/high")
                .param("date", "2022-01-05")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    void getCryptoHighShouldReturn401() throws Exception {
        mockMvc.perform(get("/crypto/high")
                .param("date", "2022-01-05")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getCryptoHighShouldReturn5XX() throws Exception {
        when(cryptoService.getHighestNormalizedCryptorForDay(LocalDate.of(2022, 1, 5))).thenThrow(new RuntimeException());
        String expectedJson = """
            {"message":"Unexpected exception"}
            """;
        mockMvc.perform(get("/crypto/high")
                .param("date", "2022-01-05")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedJson));
    }

}
