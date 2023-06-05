package team.mosk.api.server.domain.subscribe.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeHistoryRepository extends JpaRepository<SubscribeHistory, Long>, CustomSubscribeHistoryRepository {
}
