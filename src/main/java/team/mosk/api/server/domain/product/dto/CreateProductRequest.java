package team.mosk.api.server.domain.product.dto;

import lombok.*;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.vo.Selling;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateProductRequest {

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;


    private String description;

    @NotNull(message = "가격 설정은 필수입니다.")
    private Long price;

    @NotNull(message = "카테고리 아이디는 필수입니다.")
    private Long categoryId;

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .selling(Selling.SELLING)
                .build();
    }
}
