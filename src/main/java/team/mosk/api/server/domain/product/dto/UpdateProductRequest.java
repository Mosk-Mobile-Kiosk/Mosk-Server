package team.mosk.api.server.domain.product.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateProductRequest {

    @NotBlank(message = "상품 아이디는 필수입니다.")
    private Long productId;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    private String description;

    @NotBlank(message = "가격 설정은 필수입니다.")
    private Long price;
}
