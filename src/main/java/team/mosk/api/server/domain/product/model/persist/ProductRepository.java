package team.mosk.api.server.domain.product.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mosk.api.server.domain.product.dto.ProductResponse;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
    List<Product> findByStoreId(Long storeId);
}
