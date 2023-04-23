package team.mosk.api.server.domain.store.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByEmail(String email);

    boolean existsByCrn(String crn);

    Optional<CustomUserDetails> findUserDetailsByEmail(String email);
}
