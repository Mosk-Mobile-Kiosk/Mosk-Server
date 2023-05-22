package team.mosk.api.server.domain.product.model.persist;

import team.mosk.api.server.domain.product.dto.ProductImgResponse;

import java.util.Optional;

public interface CustomProductImgRepository {
    Optional<ProductImg> findImgByProductId(final Long productId);
}
