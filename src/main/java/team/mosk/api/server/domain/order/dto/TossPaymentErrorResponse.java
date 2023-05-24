package team.mosk.api.server.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentErrorResponse {
    private int code;
    private String message;
}
