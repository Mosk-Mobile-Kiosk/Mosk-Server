package team.mosk.api.server.domain.order.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import team.mosk.api.server.domain.order.dto.TossPaymentCancelRequest;
import team.mosk.api.server.domain.order.dto.TossPaymentErrorResponse;
import team.mosk.api.server.domain.order.dto.TossPaymentRequest;
import team.mosk.api.server.domain.order.error.TossApiException;

@Service
public class PaymentService {
    private final WebClient webClient;

    public PaymentService(@Qualifier("tossClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public void paymentApproval(TossPaymentRequest request) {
        webClient.post()
                .uri("confirm")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    response.bodyToMono(TossPaymentErrorResponse.class)
                            .flatMap(errorResponse -> {
                                return Mono.error(new TossApiException(errorResponse.getMessage()));
                            });
                    return null;
                });
    }

    public void paymentCancel(String paymentKey, String reason) {
        webClient.post()
                .uri(paymentKey + "/cancel")
                .bodyValue(new TossPaymentCancelRequest(reason))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    response.bodyToMono(TossPaymentErrorResponse.class)
                            .flatMap(errorResponse -> {
                                return Mono.error(new TossApiException(errorResponse.getMessage()));
                            });
                    return null;
                });
    }
}
