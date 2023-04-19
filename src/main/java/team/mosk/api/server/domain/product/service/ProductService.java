package team.mosk.api.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.mosk.api.server.domain.product.dto.ProductImgResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.UpdateProductRequest;
import team.mosk.api.server.domain.product.model.persist.Product;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    public ProductResponse create(final Product product, final Long categoryId) {
        return null;
    }

    public ProductResponse update(final UpdateProductRequest request, final Long storeId) {
        return null;
    }

    public void delete(final Long productId, final Long storeId) {
    }

    /**
     * files
     */

    public ProductImgResponse updateImg(final MultipartFile newFile, final Long productId, final Long storeId) {
        return null;
    }
}
