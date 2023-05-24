package team.mosk.api.server.domain.options.optionGroup.util;

import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.util.GivenOption;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.product.model.persist.Product;

import java.util.ArrayList;
import java.util.List;

public class GivenOptionGroup {
    public static final String GROUP_NAME = "테스트 그룹";

    public static final String MODIFIED_GROUP_NAME = "NEW 테스트 그룹";

    public static OptionGroup toEntity() {
        return OptionGroup.builder()
                .name(GROUP_NAME)
                .options(new ArrayList<>())
                .build();
    }

    public static OptionGroup toEntityWithCount() {
        List<Option> options = new ArrayList<>();
        options.add(GivenOption.toEntityWithCount());

        Product product = Product.builder()
                .id(1L)
                .name("TEST")
                .build();

        return OptionGroup.builder()
                .id(1L)
                .name(GROUP_NAME)
                .options(options)
                .product(product)
                .build();
    }

    public static OptionGroup toEntityForUpdateTest() {
        return OptionGroup.builder()
                .id(1L)
                .name(MODIFIED_GROUP_NAME)
                .build();
    }
}
