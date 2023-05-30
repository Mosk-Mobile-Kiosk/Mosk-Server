package team.mosk.api.server.domain.subscribe.model.persist;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static team.mosk.api.server.domain.subscribe.model.persist.QSubscribeHistory.*;

@Repository
@RequiredArgsConstructor
public class CustomSubscribeHistoryRepositoryImpl implements CustomSubscribeHistoryRepository {

    private final JPAQueryFactory query;
    @Override
    public List<SubscribeHistory> findAllByStoreId(Long storeId) {
        return query.select(subscribeHistory)
                .from(subscribeHistory)
                .where(subscribeHistory.store.id.eq(storeId))
                .fetch();
    }
}
