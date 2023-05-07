package team.mosk.api.server.domain.product.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.ProductSearch;
import team.mosk.api.server.domain.product.dto.ProductSearchFromCategory;

import java.util.List;
import java.util.Optional;

import static team.mosk.api.server.domain.product.model.persist.QProduct.*;
import static team.mosk.api.server.domain.product.model.persist.expression.ProductExpression.*;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

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
    public List<ProductResponse> findAllByCategoryIdEachStore(final ProductSearchFromCategory productSearchFromCategory) {
        return query.select(Projections.constructor(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("name"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(EQ_CATEGORY_ID.eqProductField(productSearchFromCategory.getCategoryId()),
                        EQ_STORE_ID.eqProductField(productSearchFromCategory.getStoreId()))
                .fetch();
    }

    @Override
    public Optional<ProductResponse> findByProductIdAndStoreId(final ProductSearch productSearch) {
        return Optional.ofNullable(query.select(Projections.constructor(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("store"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(EQ_PRODUCT_ID.eqProductField(productSearch.getProductId()),
                        EQ_STORE_ID.eqProductField(productSearch.getStoreId()))
                .fetchOne());
    }

    @Override
    public List<ProductResponse> findProductsHasKeyword(final Long storeId, final String keyword) {
        return query.select(Projections.constructor(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("name"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(EQ_STORE_ID.eqProductField(storeId),
                        hasKeyword(keyword))
                .fetch();
    }

    public BooleanExpression hasKeyword(final String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return product.name.contains(keyword);
    }
}
