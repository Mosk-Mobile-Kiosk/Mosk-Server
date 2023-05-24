package team.mosk.api.server.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentCancelRequest {
    private String cancelReason;

    public TossPaymentCancelRequest(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
