package team.mosk.api.server.domain.product.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.model.persist.QOptionGroup;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.model.persist.expression.ProductExpression;

import java.util.List;

import static team.mosk.api.server.domain.options.option.model.persist.QOption.option;
import static team.mosk.api.server.domain.options.optionGroup.model.persist.QOptionGroup.optionGroup;
import static team.mosk.api.server.domain.product.model.persist.QProduct.*;
import static team.mosk.api.server.domain.product.model.persist.expression.ProductExpression.*;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory query;


    @Override
    public ProductResponse findByProductId(final Long id) {
        Product findProduct = query.select(product)
                .from(product)
                .join(product.optionGroups, optionGroup)
                .fetchJoin()
                .where(EQ_PRODUCT_ID.eqProductField(id))
                .fetchOne();

        return ProductResponse.of(findProduct);
    }

    @Override
    public List<ProductResponse> findByKeyword(final String keyword, final Long storeId) {
        List<Product> fetch = query.select(product)
                .from(product)
                .join(product.optionGroups, optionGroup)
                .fetchJoin()
                .where(hasKeyword(keyword),
                        EQ_STORE_ID.eqProductField(storeId))
                .fetch();

        return fetch.stream().map(ProductResponse::of).toList();
    }


    public BooleanExpression hasKeyword(final String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return product.name.contains(keyword);
    }
}
