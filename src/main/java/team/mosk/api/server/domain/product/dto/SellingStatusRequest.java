package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.product.model.vo.Selling;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SellingStatusRequest {

    @NotBlank(message = "상품 아이디는 필수입니다.")
    private Long productId;

    @NotBlank(message = "상태는 필수입니다.")
    private Selling selling;
}
