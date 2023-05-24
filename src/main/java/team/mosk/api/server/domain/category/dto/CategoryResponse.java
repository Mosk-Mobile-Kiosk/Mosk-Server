package team.mosk.api.server.domain.category.dto;

import lombok.*;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.model.persist.Product;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private String storeName;

    private List<ProductResponse> products;

    public static CategoryResponse of(final Category category) {
        List<ProductResponse> products = category.getProducts().stream()
                .map(ProductResponse::of).toList();

        return new CategoryResponse(category.getId(), category.getName(), category.getStore().getStoreName(), products);
    }
}
