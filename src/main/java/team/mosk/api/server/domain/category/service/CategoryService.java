package team.mosk.api.server.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private static final String OWNER_MISMATCHED = "상점의 주인이 아닙니다.";
    private static final String CATEGORY_NOT_FOUND = "카테고리를 찾을 수 없습니다.";
    private static final String STORE_NOT_FOUND = "상점을 찾을 수 없습니다.";

    public CategoryResponse create(final Category category, final Long storeId) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(STORE_NOT_FOUND));

        category.initStore(findStore);
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.of(savedCategory);
    }

    public void delete(final Long categoryId, final Long storeId) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        validateStoreOwner(findCategory.getStore().getId(), storeId);
        categoryRepository.delete(findCategory);
    }

    public CategoryResponse update(final UpdateCategoryRequest request, final Long storeId) {
        Category findCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        validateStoreOwner(findCategory.getStore().getId(), storeId);
        findCategory.update(request);

        return CategoryResponse.of(findCategory);
    }

    public void validateStoreOwner(final Long storeId, final Long targetId) {
        if (!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(OWNER_MISMATCHED);
        }
    }
}