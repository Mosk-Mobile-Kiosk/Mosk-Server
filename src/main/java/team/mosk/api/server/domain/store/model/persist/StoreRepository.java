package team.mosk.api.server.domain.store.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByEmail(String email);

    boolean existsByCrn(String crn);
}
