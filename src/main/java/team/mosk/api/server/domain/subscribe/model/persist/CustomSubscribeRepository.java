package team.mosk.api.server.domain.subscribe.model.persist;

import java.util.Optional;

public interface CustomSubscribeRepository {

    Optional<Subscribe> findByStoreId(final Long storeId);
}
