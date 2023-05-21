package team.mosk.api.server.domain.product.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;

import java.util.List;

import static team.mosk.api.server.domain.options.option.model.persist.QOption.option;
import static team.mosk.api.server.domain.options.optionGroup.model.persist.QOptionGroup.optionGroup;
import static team.mosk.api.server.domain.product.model.persist.QProduct.*;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory query;


    @Override
    public ProductResponse findByProductId(final Long id) {
        List<OptionGroupResponse> groups = query
                .select(Projections.constructor(OptionGroupResponse.class,
                        optionGroup.id,
                        optionGroup.name,
                        Projections.list(Projections.constructor(OptionResponse.class,
                                option.id,
                                option.name,
                                option.price)),
                        optionGroup.product.name))
                .from(optionGroup)
                .leftJoin(optionGroup.options, option)
                .where(optionGroup.product.id.eq(id))
                .groupBy(optionGroup.id)
                .fetch();

        groups.forEach(og -> {
            og.setOptions(query
                    .select(Projections.constructor(OptionResponse.class,
                            option.id,
                            option.name,
                            option.price))
                    .from(option)
                    .where(option.optionGroup.id.eq(og.getId()))
                    .fetch());
        });

        ProductResponse response = query.select(Projections.bean(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("name"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(product.id.eq(id))
                .fetchOne();

        if (response != null) {
            response.setOptionGroups(groups);
        }

        return response;
    }

    @Override
    public ProductResponse findByKeyword(String keyword, Long storeId) {
        List<OptionGroupResponse> groups = query
                .select(Projections.constructor(OptionGroupResponse.class,
                        optionGroup.id,
                        optionGroup.name,
                        Projections.list(Projections.constructor(OptionResponse.class,
                                option.id,
                                option.name,
                                option.price)),
                        optionGroup.product.name))
                .from(optionGroup)
                .leftJoin(optionGroup.options, option)
                .where(optionGroup.product.name.eq(keyword),
                        optionGroup.product.store.id.eq(storeId))
                .groupBy(optionGroup.id)
                .fetch();

        groups.forEach(og -> {
            og.setOptions(query
                    .select(Projections.constructor(OptionResponse.class,
                            option.id,
                            option.name,
                            option.price))
                    .from(option)
                    .where(option.optionGroup.id.eq(og.getId()))
                    .fetch());
        });

        ProductResponse response = query.select(Projections.bean(ProductResponse.class,
                        product.id.as("id"),
                        product.name.as("name"),
                        product.description.as("description"),
                        product.price.as("price"),
                        product.selling.as("selling")))
                .from(product)
                .where(hasKeyword(keyword),
                        product.store.id.eq(storeId))
                .fetchOne();

        if (response != null) {
            response.setOptionGroups(groups);
        }

        return response;
    }


    public BooleanExpression hasKeyword(final String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        return product.name.contains(keyword);
    }
}
