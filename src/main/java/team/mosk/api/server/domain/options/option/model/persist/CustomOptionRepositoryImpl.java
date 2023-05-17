package team.mosk.api.server.domain.options.option.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.optionGroup.model.persist.QOptionGroup;

import java.util.List;

import static team.mosk.api.server.domain.options.option.model.persist.QOption.*;

@Repository
@RequiredArgsConstructor
public class CustomOptionRepositoryImpl implements CustomOptionRepository {

    private final JPAQueryFactory query;

    @Override
    public List<OptionResponse> findAllOptionByGroupId(final Long groupId) {
        return query.select(Projections.constructor(OptionResponse.class,
                option.id.as("id"),
                option.name.as("name"),
                option.price.as("price")))
                .from(option)
                .where(option.optionGroup.id.eq(groupId))
                .fetch();
    }
}
