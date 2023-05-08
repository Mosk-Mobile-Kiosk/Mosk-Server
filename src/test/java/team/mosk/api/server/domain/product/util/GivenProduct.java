package team.mosk.api.server.domain.product.util;

import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.vo.Selling;

public class GivenProduct {

    public static final String PRODUCT_NAME = "테스트 상품";
    public static final String PRODUCT_DESCRIPTION = "테스트 설명";
    public static final Long PRODUCT_PRICE = 100L;

    public static final Selling PRODUCT_SELLING_STATUS = Selling.SELLING;

    public static Product toEntity() {
        return Product.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .selling(PRODUCT_SELLING_STATUS)
                .build();
    }

    public static Product toEntityWithProductCount() {
        return Product.builder()
                .id(1L)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .selling(PRODUCT_SELLING_STATUS)
                .build();
    }

    public static Product toEntityForUpdateTest() {
        return Product.builder()
                .id(1L)
                .name("New 테스트 상품")
                .description("New 테스트 설명")
                .price(50L)
                .selling(Selling.SOLDOUT)
                .build();
    }
}
