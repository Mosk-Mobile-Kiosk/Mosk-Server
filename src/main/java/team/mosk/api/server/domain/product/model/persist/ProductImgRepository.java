package team.mosk.api.server.domain.product.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

    Optional<ProductImg> findProductImgByProduct_Id(final Long productId);
}
