package team.mosk.api.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.product.dto.ProductImgResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.ProductSearch;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductReadService {

    public ProductResponse findByProductId(final Long productId) {
        return null;
    }

    public Page<ProductResponse> findAllWithPaging(final String storeName, final Pageable pageable) {
        return null;
    }

    public List<ProductResponse> findAllByCategoryNameEachStore(final ProductSearch productSearch) {
        return null;
    }


    /**
     * files
     */

    public ProductImgResponse findImgByProductId(final Long productId) {
        return null;
    }
}
