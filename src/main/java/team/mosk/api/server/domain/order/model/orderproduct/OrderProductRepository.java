package team.mosk.api.server.domain.order.model.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mosk.api.server.domain.order.model.orderproductoption.OrderProductOption;

public interface OrderProductRepository extends JpaRepository<OrderProductOption, Long> {
}
