package integrationtest;

import com.xmcy.application.CryptoApplication;
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

@SpringBootTest(
    classes = CryptoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"xmcy.supported.cryptos=BTC,ETH,LTC,XRP,DOGE,DUMMY"})
@AutoConfigureMockMvc
class GetCryptoLimitsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private CryptoRecommendationService cryptoService;

    @WithMockUser
    @Test
    void getCryptoLimitsShouldReturn200() throws Exception {
        String expectedJson = """
            {"max":{"date":"2022-01-02","price":47722.66,"symbol":"BTC"},"min":{"date":"2022-01-24","price":33276.59,"symbol":"BTC"},"newest":{"date":"2022-01-31","price":38415.79,"symbol":"BTC"},"oldest":{"date":"2022-01-01","price":46813.21,"symbol":"BTC"}}
            """;
        mockMvc.perform(get("/crypto/limits")
                .param("symbol", "BTC")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    void getCryptoLimitsShouldReturn401() throws Exception {
        mockMvc.perform(get("/crypto/limits")
                .param("symbol", "BTC")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getCryptoLimitsShouldReturn404() throws Exception {
        String expectedJson = """
            {"message":"Crypto not found"}
            """;
        mockMvc.perform(get("/crypto/limits")
                .param("symbol", "DUMMY")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void getCryptoLimitsShouldReturn400() throws Exception {
        String expectedJson = """
            {"message":"Invalid input"}
            """;
        mockMvc.perform(get("/crypto/limits")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void getCryptoLimitsShouldReturn400WhenUnsupportedCrypto() throws Exception {
        String expectedJson = """
            {"message":"Invalid input"}
            """;
        mockMvc.perform(get("/crypto/limits")
                .param("symbol", "NOT_SUPPORTED")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void getCryptoLimitsShouldReturn5XX() throws Exception {
        when(cryptoService.getCryptoLimits("BTC")).thenThrow(new RuntimeException());
        String expectedJson = """
            {"message":"Unexpected exception"}
            """;
        mockMvc.perform(get("/crypto/limits")
                .param("symbol", "BTC")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedJson));
    }

}
