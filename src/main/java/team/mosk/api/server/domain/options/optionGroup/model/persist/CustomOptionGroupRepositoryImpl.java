package team.mosk.api.server.domain.options.optionGroup.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;

import java.util.List;

import static team.mosk.api.server.domain.options.option.model.persist.QOption.*;
import static team.mosk.api.server.domain.options.optionGroup.model.persist.QOptionGroup.*;
import static team.mosk.api.server.domain.product.model.persist.QProduct.*;

@Repository
@RequiredArgsConstructor
public class CustomOptionGroupRepositoryImpl implements CustomOptionGroupRepository {

    private final JPAQueryFactory query;

    @Override
    public List<OptionGroupResponse> findAllOptionGroupByProductId(final Long productId) {
        List<OptionGroupResponse> response = query
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
                .where(optionGroup.product.id.eq(productId))
                .groupBy(optionGroup.id)
                .fetch();

        for(OptionGroupResponse ogr : response) {
            for(OptionResponse or : ogr.getOptions()) {
                System.out.println(or.getId());
                System.out.println(or.getName());
                System.out.println(or.getPrice());
            }
        }

        return response;
    }
}
