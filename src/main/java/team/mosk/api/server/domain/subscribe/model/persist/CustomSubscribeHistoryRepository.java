package team.mosk.api.server.domain.subscribe.model.persist;

import java.util.List;
import java.util.Optional;

public interface CustomSubscribeHistoryRepository {
    List<SubscribeHistory> findAllByStoreId(final Long storeId);
}
