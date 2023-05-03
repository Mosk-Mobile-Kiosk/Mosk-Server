package team.mosk.api.server.domain.product.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductSearch {

    @NotBlank
    private Long productId;

    @NotBlank
    private Long storeId;
}
