package integrationtest;

import com.xmcy.application.CryptoApplication;
import com.xmcy.service.CryptoRecommendationService;
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

@SpringBootTest(classes = CryptoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.MethodName.class)
class GetCryptosIntegrationTest {

    private static final String GET_CRYPTOS_PATH = "/cryptos";

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
        mockMvc.perform(get(GET_CRYPTOS_PATH)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedContent));
    }

    @Test
    void getCryptosShouldReturn401() throws Exception {
        mockMvc.perform(get(GET_CRYPTOS_PATH)
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
        mockMvc.perform(get(GET_CRYPTOS_PATH)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedJson));
    }

    @WithMockUser
    @Test
    void wgetCryptosShouldReturn429AfterTooManyRequests() throws Exception {
        Set<Integer> responses = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            responses.add(mockMvc.perform(get(GET_CRYPTOS_PATH)
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus());
        }

        assertThat(responses).contains(429);
    }
}
