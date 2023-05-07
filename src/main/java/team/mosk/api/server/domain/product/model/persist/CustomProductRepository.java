package team.mosk.api.server.domain.product.model.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.ProductSearchFromCategory;
import team.mosk.api.server.domain.product.dto.ProductSearch;

import java.util.List;
import java.util.Optional;

public interface CustomProductRepository {
    Page<ProductResponse> findAllWithPaging(final Long storeId, final Pageable pageable);
    List<ProductResponse> findAllByCategoryIdEachStore(final ProductSearchFromCategory productSearchFromCategory);

    Optional<ProductResponse> findByProductIdAndStoreId(final ProductSearch productSearch);

    List<ProductResponse> findProductsHasKeyword(final Long storeId, final String keyword);
}
