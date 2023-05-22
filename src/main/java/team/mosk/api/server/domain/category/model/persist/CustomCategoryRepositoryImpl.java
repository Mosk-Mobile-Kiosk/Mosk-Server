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
    public CategoryResponse findByCategoryId(final Long id) {
        Category findCategory = query.select(category)
                .from(category)
                .where(category.id.eq(id))
                .fetchOne();

        return CategoryResponse.of(findCategory);
    }

    @Override
    public List<CategoryResponse> findAllByStoreId(final Long storeId) {
        List<Category> categories = query.select(category)
                .from(category)
                .where(category.store.id.eq(storeId))
                .fetch();

        return categories.stream().map(CategoryResponse::of).toList();
    }
}
