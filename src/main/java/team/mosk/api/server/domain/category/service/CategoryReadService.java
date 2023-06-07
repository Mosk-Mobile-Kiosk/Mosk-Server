package team.mosk.api.server.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryReadService {

    private final CategoryRepository categoryRepository;

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
}
