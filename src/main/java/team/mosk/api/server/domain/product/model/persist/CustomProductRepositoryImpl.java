package team.mosk.api.server.domain.product.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.ProductSearch;
import team.mosk.api.server.domain.product.model.persist.expression.ProductExpression;

import java.util.List;

import static team.mosk.api.server.domain.product.model.persist.QProduct.*;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository{

    private final JPAQueryFactory query;

    @Override
    public Page<ProductResponse> findAllWithPaging(final Long storeId, final Pageable pageable) {
        List<ProductResponse> products = query.select(Projections.constructor(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("name"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(product.store.id.eq(storeId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> count = query.select(product.count())
                .from(product)
                .where(product.store.id.eq(storeId));

        return PageableExecutionUtils.getPage(products, pageable, count::fetchFirst);
    }

    @Override
    public List<ProductResponse> findAllByCategoryNameEachStore(final ProductSearch productSearch) {
        return query.select(Projections.constructor(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("name"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(product.store.id.eq(productSearch.getStoreId()),
                        ProductExpression.EQ_CATEGORY_NAME.eqProductField(productSearch.getCategoryName()))
                .fetch();
    }
}
