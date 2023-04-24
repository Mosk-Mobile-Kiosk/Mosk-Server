package team.mosk.api.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.product.dto.ProductImgResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.ProductSearch;
import team.mosk.api.server.domain.product.error.ProductImgNotFoundException;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductReadService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    private static final String PRODUCT_NOT_FOUND = "상품을 찾을 수 없습니다.";
    private static final String PRODUCT_IMG_NOT_FOUND = "이미지를 찾을 수 없습니다.";

    public ProductResponse findByProductId(final Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        return ProductResponse.of(findProduct);
    }

    public Page<ProductResponse> findAllWithPaging(final Long storeId, final Pageable pageable) {
        // TODO: 2023-04-20 QueryDSL 추가 후 작업
        return null;
    }

    public List<ProductResponse> findAllByCategoryNameEachStore(final ProductSearch productSearch) {
        // TODO: 2023-04-20 QueryDSL 추가 후 작업
        return null;
    }


    /**
     * files
     */

    public ProductImgResponse findImgByProductId(final Long productId) throws IOException {
        ProductImg findProductImg = productImgRepository.findProductImgByProduct_Id(productId)
                .orElseThrow(() -> new ProductImgNotFoundException(PRODUCT_IMG_NOT_FOUND));

        return ProductImgResponse.of(findProductImg);
    }
}
