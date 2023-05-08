package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.product.model.vo.Selling;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SellingStatusRequest {

    @NotNull(message = "상품 아이디는 필수입니다.")
    private Long productId;

    @NotNull(message = "상태는 필수입니다.")
    private Selling selling;
}
