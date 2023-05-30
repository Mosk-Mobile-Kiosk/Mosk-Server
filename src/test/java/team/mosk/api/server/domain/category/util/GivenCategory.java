package team.mosk.api.server.domain.category.util;

import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.util.GivenStore;

import java.util.ArrayList;

public class GivenCategory {
    public static final String GIVEN_NAME = "테스트 카테고리";
    public static final String NEW_NAME = "New 테스트 카테고리";

    public static Category toEntity() {
        return Category.builder()
                .name(GIVEN_NAME)
                .products(new ArrayList<>())
                .build();
    }

    public static Category toEntityWithBlankName() {
        return Category.builder()
                .name("")
                .build();
    }

    public static Category toEntityWithCategoryCount() {
        Store dummy = Store.builder()
                .id(1L)
                .build();

        return Category.builder()
                .id(1L)
                .name(GIVEN_NAME)
                .products(new ArrayList<>())
                .store(dummy)
                .build();
    }

    public static Category toEntityForUpdateTest() {
        Store dummy = Store.builder()
                .id(1L)
                .build();

        return Category.builder()
                .id(1L)
                .name(NEW_NAME)
                .products(new ArrayList<>())
                .store(dummy)
                .build();
    }
}
