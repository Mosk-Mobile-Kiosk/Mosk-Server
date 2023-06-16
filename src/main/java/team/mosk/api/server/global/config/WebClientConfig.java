package team.mosk.api.server.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.UnsupportedEncodingException;

@Configuration
public class WebClientConfig {

    private final String tossApiKey;

    public WebClientConfig(@Value("${apiKey.toss}") String tossApiKey) {
        this.tossApiKey = tossApiKey;
    }

    @Bean(name = "gongGongDataClient")
    public WebClient gongGongDataClient() {
        String baseUrl = "https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=y3VIhvpKbdXiPhDgY5dMR8eIRvYV3Tg2zy7ySnet4Ow4rI%2BT%2FwXYfuZ3o5r9OwL9355ZQhBNTVmC54Ng%2B6PsEg%3D%3D";
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "qrCodeClient")
    public WebClient qrCodeClient() throws UnsupportedEncodingException {
        return WebClient.builder()
                .baseUrl("https://chart.googleapis.com/chart")
                .defaultHeader("Content-Type", MediaType.IMAGE_PNG_VALUE)
                .build();
    }

    @Bean(name = "tossClient")
    public WebClient tossClient() {
        return WebClient.builder()
                .baseUrl("https://api.tosspayments.com/v1/payments/")
                .defaultHeaders(headers -> {
                            headers.set("Authorization", "Basic " + tossApiKey);
                            headers.set("Content-Type", "application/json");
                        }
                )
                .build();
    }


}
