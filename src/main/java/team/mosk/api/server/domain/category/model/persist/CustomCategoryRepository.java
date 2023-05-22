package team.mosk.api.server.domain.category.model.persist;

import team.mosk.api.server.domain.category.dto.CategoryResponse;

import java.util.List;

public interface CustomCategoryRepository {

    CategoryResponse findByCategoryId(final Long id);
    List<CategoryResponse> findAllByStoreId(final Long storeId);
}
