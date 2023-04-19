package team.mosk.api.server.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.category.dto.SimpleCategoryResponse;
import team.mosk.api.server.domain.category.model.persist.Category;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    public SimpleCategoryResponse create(final Category category, final Long storeId) {
        return null;
    }

    public void delete(final Long categoryId, final Long storeId) {
    }

    public SimpleCategoryResponse update(final UpdateCategoryRequest request, final Long storeId) {
        return null;
    }
}
