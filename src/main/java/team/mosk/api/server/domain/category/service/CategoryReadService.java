package team.mosk.api.server.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.CategoryResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryReadService {

    public List<CategoryResponse> findAllByStoreId(final Long storeId) {
        // TODO: 2023-04-20 QueryDSL 반영 후 작업
        return null;
    }
}
