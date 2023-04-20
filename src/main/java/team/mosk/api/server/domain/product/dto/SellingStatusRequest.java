package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.product.model.vo.Selling;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SellingStatusRequest {
    private Long productId;

    private Selling selling;
}
