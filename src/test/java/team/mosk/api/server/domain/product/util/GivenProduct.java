package team.mosk.api.server.domain.product.util;

import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.vo.Selling;

import java.util.ArrayList;

public class GivenProduct {

    public static final String PRODUCT_NAME = "테스트 상품";

    public static final String MODIFIED_PRODUCT_NAME = "NEW 테스트 상품";
    public static final String PRODUCT_DESCRIPTION = "테스트 설명";
    public static final String MODIFIED_PRODUCT_DESCRIPTION = "NEW 테스트 설명";
    public static final Long PRODUCT_PRICE = 100L;
    public static final Long MODIFIED_PRODUCT_PRICE = 50L;

    public static final Selling PRODUCT_SELLING_STATUS = Selling.SELLING;

    public static Product toEntity() {
        return Product.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .selling(PRODUCT_SELLING_STATUS)
                .optionGroups(new ArrayList<>())
                .build();
    }

    public static Product toEntityWithProductCount() {
        return Product.builder()
                .id(1L)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .selling(PRODUCT_SELLING_STATUS)
                .optionGroups(new ArrayList<>())
                .build();
    }

    public static Product toEntityForUpdateTest() {
        return Product.builder()
                .id(1L)
                .name(MODIFIED_PRODUCT_NAME)
                .description(MODIFIED_PRODUCT_DESCRIPTION)
                .price(MODIFIED_PRODUCT_PRICE)
                .selling(Selling.SOLDOUT)
                .optionGroups(new ArrayList<>())
                .build();
    }
}
