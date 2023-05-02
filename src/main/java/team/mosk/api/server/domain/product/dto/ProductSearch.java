package team.mosk.api.server.domain.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProductSearch {

    private Long storeId;

    private String categoryName;
}
