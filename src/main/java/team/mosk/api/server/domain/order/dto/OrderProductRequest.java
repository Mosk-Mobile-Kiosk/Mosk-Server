package team.mosk.api.server.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderProductRequest {

    @NotNull(message = "상품번호는 필수값 입니다.")
    private Long productId;

    private List<Long> optionIds;

    @Min(value = 1, message = "상품주문 갯수는 1개 이상이여야 합니다.")
    private int quantity;

    @Builder
    public OrderProductRequest(Long productId, List<Long> optionIds, int quantity) {
        this.productId = productId;
        this.optionIds = optionIds;
        this.quantity = quantity;
    }
}
