package team.mosk.api.server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${gongGongData.apiKey}")
    private String gongGongApiKey;

    @Bean(name = "gongGongDataClient")
    public WebClient gongGongDataClient() {
        return WebClient.builder()
                .baseUrl("https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=" + gongGongApiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "qrCodeClient")
    public WebClient qrCodeClient() {
        return WebClient.builder()
                .baseUrl("https://chart.googleapis.com")
                .defaultHeader("Content-Type", MediaType.IMAGE_PNG_VALUE)
                .build();
    }
}
