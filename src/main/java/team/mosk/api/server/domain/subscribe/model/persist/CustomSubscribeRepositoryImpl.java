package team.mosk.api.server.domain.subscribe.model.persist;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static team.mosk.api.server.domain.subscribe.model.persist.QSubscribe.*;

@Repository
@RequiredArgsConstructor
public class CustomSubscribeRepositoryImpl implements CustomSubscribeRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<Subscribe> findByStoreId(final Long storeId) {
        return Optional.ofNullable(query.select(subscribe)
                .from(subscribe)
                .where(subscribe.store.id.eq(storeId))
                .fetchOne());
    }
}
