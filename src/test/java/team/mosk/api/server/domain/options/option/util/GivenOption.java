package team.mosk.api.server.domain.options.option.util;

import team.mosk.api.server.domain.options.option.model.persist.Option;

public class GivenOption {
    public static final String OPTION_NAME = "테스트 옵션";

    public static final String MODIFIED_OPTION_NAME = "NEW 테스트 옵션";

    public static final int OPTION_PRICE = 1000;
    public static final int MODIFIED_OPTION_PRICE = 10000;

    public static Option toEntity() {
        return Option.builder()
                .name(OPTION_NAME)
                .price(OPTION_PRICE)
                .build();
    }

    public static Option toEntityWithCount() {
        return Option.builder()
                .id(1L)
                .name(OPTION_NAME)
                .price(OPTION_PRICE)
                .build();
    }

    public static Option toEntityForUpdateTest() {
        return Option.builder()
                .id(1L)
                .name(MODIFIED_OPTION_NAME)
                .price(MODIFIED_OPTION_PRICE)
                .build();
    }
}
