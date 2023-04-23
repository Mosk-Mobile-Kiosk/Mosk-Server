package team.mosk.api.server.domain.store.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
    Optional<QRCode> findByStore(Store store);
}
