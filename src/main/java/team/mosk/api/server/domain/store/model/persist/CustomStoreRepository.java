package team.mosk.api.server.domain.store.model.persist;

import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.Optional;

public interface CustomStoreRepository {
    Optional<CustomUserDetails> findUserDetailsByEmail(String email);

    Optional<CustomUserDetails> findUserDetailsById(Long id);
}
