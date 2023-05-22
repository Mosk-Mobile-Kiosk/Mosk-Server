package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.order.model.Order;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class TossPaymentRequest {

  private String paymentKey; //결제 키값 최대 200자
  private String orderId; // 주문 ID입니다. 충분히 무작위한 값을 직접 생성해서 사용
  private int amount; // 결제할 금액

  public static TossPaymentRequest of(Order order, String paymentKey) {
    return TossPaymentRequest.builder()
            .paymentKey(paymentKey)
            .orderId(UUID.randomUUID().toString())
            .amount(order.getTotalPrice())
            .build();
  }

  @Builder
  public TossPaymentRequest(String paymentKey, String orderId, int amount) {
    this.paymentKey = paymentKey;
    this.orderId = orderId;
    this.amount = amount;
  }

}
