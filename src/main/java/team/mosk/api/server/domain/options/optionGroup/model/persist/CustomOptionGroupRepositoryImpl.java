package team.mosk.api.server.domain.options.optionGroup.model.persist;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;

import java.util.List;

import static team.mosk.api.server.domain.options.optionGroup.model.persist.QOptionGroup.*;

@Repository
@RequiredArgsConstructor
public class CustomOptionGroupRepositoryImpl implements CustomOptionGroupRepository {

    private final JPAQueryFactory query;

    @Override
    public List<OptionGroupResponse> findAllOptionGroup() {
        return query.select(Projections.constructor(OptionGroupResponse.class,
                        optionGroup.id.as("id"),
                        optionGroup.name.as("name")))
                .from(optionGroup)
                .fetch();
    }
}
