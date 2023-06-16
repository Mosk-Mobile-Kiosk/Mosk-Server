package team.mosk.api.server.domain.product.model.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.ProductSearch;

import java.util.List;
import java.util.Optional;

public interface CustomProductRepository {

    ProductResponse findByProductId(final Long id);

    List<ProductResponse> findByKeyword(final String keyword, final Long storeId);
}
