package team.mosk.api.server.domain.orderproduct.model;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mosk.api.server.domain.orderproductoption.model.OrderProductOption;

public interface OrderProductRepository extends JpaRepository<OrderProductOption, Long> {
}
