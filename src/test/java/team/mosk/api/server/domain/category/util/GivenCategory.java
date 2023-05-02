package team.mosk.api.server.domain.category.util;

import team.mosk.api.server.domain.category.model.persist.Category;

public class GivenCategory {
    public static final String GIVEN_NAME = "테스트 카테고리";
    public static final String NEW_NAME = "New 테스트 카테고리";

    public static Category toEntity() {
        return Category.builder()
                .name(GIVEN_NAME)
                .build();
    }

    public static Category toEntityWithCategoryCount() {
        return Category.builder()
                .id(1L)
                .name(GIVEN_NAME)
                .build();
    }

    public static Category toEntityForUpdateTest() {
        return Category.builder()
                .id(1L)
                .name(NEW_NAME)
                .build();
    }
}
