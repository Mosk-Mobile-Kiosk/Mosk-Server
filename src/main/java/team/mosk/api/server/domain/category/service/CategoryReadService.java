package team.mosk.api.server.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;

import java.util.List;

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
}
