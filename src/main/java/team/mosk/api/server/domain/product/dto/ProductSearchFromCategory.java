package team.mosk.api.server.domain.product.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductSearchFromCategory {

    @NotBlank
    private Long storeId;

    @NotBlank
    private Long categoryId;
}
