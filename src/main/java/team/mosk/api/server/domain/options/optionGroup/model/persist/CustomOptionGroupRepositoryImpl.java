package team.mosk.api.server.domain.options.optionGroup.model.persist;

import com.querydsl.core.Tuple;
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
        List<OptionGroup> fetch = query
                .select(optionGroup)
                .from(optionGroup)
                .leftJoin(optionGroup.options, option)
                .where(optionGroup.product.id.eq(productId))
                .groupBy(optionGroup.id)
                .fetch();

        return fetch.stream().map(OptionGroupResponse::of).toList();
    }
}
