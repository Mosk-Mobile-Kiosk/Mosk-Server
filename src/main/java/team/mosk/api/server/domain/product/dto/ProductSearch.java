package team.mosk.api.server.domain.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSearch {

    private String storeName;

    private String categoryName;
}
