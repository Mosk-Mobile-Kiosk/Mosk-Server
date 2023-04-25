package team.mosk.api.server.domain.category.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;

import java.util.List;

import static team.mosk.api.server.domain.category.model.persist.QCategory.*;

@Repository
@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<CategoryResponse> findAllByStoreId(final Long storeId) {
        List<CategoryResponse> categories = query.select(Projections.constructor(CategoryResponse.class,
                        category.id.as("id"),
                        category.name.as("name")))
                .from(category)
                .where(category.store.id.eq(storeId))
                .fetch();

        if (categories.size() == 0) {
            throw new CategoryNotFoundException("상점에 카테고리가 존재하지 않습니다.");
        }

        return categories;
    }
}
