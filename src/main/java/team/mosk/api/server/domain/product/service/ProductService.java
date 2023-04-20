package team.mosk.api.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.product.dto.ProductImgResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.SellingStatusRequest;
import team.mosk.api.server.domain.product.dto.UpdateProductRequest;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    private static final String OWNER_MISMATCHED = "상점의 주인이 아닙니다.";
    private static final String PRODUCT_NOT_FOUND = "상품을 찾을 수 없습니다.";
    private static final String LOCAL_PATH = "C:\\Users\\Student\\Desktop\\study\\imgs\\";
    private static final String BASE_IMG_PATH= "C:\\Users\\Student\\Desktop\\study\\baseImg\\";


    public ProductResponse create(final Product product, final Long categoryId, final Long storeId) {
        // TODO: 2023-04-20 Store upstream 반영 후 작업
        // TODO: 2023-04-20 오너 인포 검증 + 상품 기본 이미지 / 카테고리 setting + 상품 저장
        return null;
    }

    public ProductResponse update(final UpdateProductRequest request, final Long storeId) {
        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        findProduct.update(request);
        return ProductResponse.of(findProduct);
    }

    public void delete(final Long productId, final Long storeId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        productRepository.delete(findProduct);
    }

    public void changeSellingStatus(final SellingStatusRequest request, final Long storeId) {
        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        if (findProduct.getSelling() != request.getSelling()) {
            findProduct.changeSellingStatus(request.getSelling());
        }
    }

    /**
     * files
     */

    public ProductImgResponse updateImg(final MultipartFile newFile, final Long productId, final Long storeId) throws IOException {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        String oldPath = findProduct.getProductImg().getPath();
        String uuid = UUID.randomUUID().toString();
        String path = LOCAL_PATH + uuid;

        ProductImg newProductImg = ProductImg.builder()
                .name(uuid)
                .path(path)
                .contentType(newFile.getContentType())
                .product(findProduct)
                .build();

        ProductImg savedProductImg = productImgRepository.save(newProductImg);

        newFile.transferTo(new File(path));

        File deleteTarget = new File(oldPath);
        deleteTarget.delete();

        return ProductImgResponse.of(savedProductImg);
    }

    public void validateStoreOwner(final Long storeId, final Long targetId) {
        if (!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(OWNER_MISMATCHED);
        }
    }
}
