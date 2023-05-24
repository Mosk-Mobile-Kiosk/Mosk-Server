package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class CreateOrderRequest {

    private String paymentKey;
    private String orderId;

    List<OrderProductRequest> orderProductRequests;

    @Builder
    public CreateOrderRequest(String paymentKey, String orderId, List<OrderProductRequest> orderProductRequests) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderProductRequests = orderProductRequests;
    }
}
