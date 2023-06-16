package team.mosk.api.server.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

import java.util.List;
import java.util.stream.Collectors;

import static team.mosk.api.server.global.error.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryReadService {

    private final CategoryRepository categoryRepository;

    private final StoreRepository storeRepository;

    public List<CategoryResponse> findAllByStoreId(final Long storeId) {
        return categoryRepository.findAllByStoreId(storeId);
    }

    public CategoryResponse findByCategoryId(final Long id) {
        return categoryRepository.findByCategoryId(id);
    }

    public List<CategoryResponse> findByStoreId(Long storeId) {
        return categoryRepository.findByStoreId(storeId).stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public CategoryResponse findByCategoryId(Long categoryId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(STORE_NOT_FOUND));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        if (!store.equals(category.getStore())) {
            throw new OwnerInfoMisMatchException(OWNER_INFO_MISMATCHED);
        }

        return new CategoryResponse(category.getId(), category.getName());
    }
}
