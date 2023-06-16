package team.mosk.api.server.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentResponse {

    private String paymentKey;
    private String orderId;
    private String orderName;
    private int  totalAmount;

}
