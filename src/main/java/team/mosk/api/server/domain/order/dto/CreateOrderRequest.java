package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@Getter
@NoArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "결제키는 필수값 입니다.")
    private String paymentKey;

    @NotBlank(message = "주문번호는 필수값 입니다.")
    private String orderId;

    @NotNull(message = "주문상품 목록은 필수값 입니다.")
    List<@Valid OrderProductRequest> orderProductRequests;

    @Builder
    public CreateOrderRequest(String paymentKey, String orderId, List<OrderProductRequest> orderProductRequests) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderProductRequests = orderProductRequests;
    }
}
