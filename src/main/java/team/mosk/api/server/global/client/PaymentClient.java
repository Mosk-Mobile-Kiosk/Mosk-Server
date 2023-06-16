package team.mosk.api.server.global.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import team.mosk.api.server.domain.order.dto.TossPaymentCancelRequest;
import team.mosk.api.server.domain.order.dto.TossPaymentErrorResponse;
import team.mosk.api.server.domain.order.dto.TossPaymentRequest;
import team.mosk.api.server.domain.order.error.PaymentGatewayException;
import team.mosk.api.server.global.error.exception.ErrorCode;

@Service
public class PaymentClient {
    private final WebClient webClient;

    private final String CANCEL_REASON = "단순변심";

    public PaymentClient(@Qualifier("tossClient") WebClient webClient) {
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
                                return Mono.error(new PaymentGatewayException(ErrorCode.PAYMENT_GATEWAY_DENIED_PAYMENT));
                            });
                    return null;
                });
    }

    public void paymentCancel(String paymentKey) {
        webClient.post()
                .uri(paymentKey + "/cancel")
                .bodyValue(new TossPaymentCancelRequest(CANCEL_REASON))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    response.bodyToMono(TossPaymentErrorResponse.class)
                            .flatMap(errorResponse -> {
                                return Mono.error(new PaymentGatewayException(ErrorCode.PAYMENT_GATEWAY_DENIED_CANCEL));
                            });
                    return null;
                });
    }
}
