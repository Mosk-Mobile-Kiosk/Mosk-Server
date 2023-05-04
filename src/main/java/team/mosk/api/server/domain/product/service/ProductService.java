package team.mosk.api.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
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
import team.mosk.api.server.domain.product.error.BasicImgInitFailedException;
import team.mosk.api.server.domain.product.error.FailedDeleteImgException;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.common.GetPathByEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    private final StoreRepository storeRepository;


    private static final String OWNER_MISMATCHED = "상점의 주인이 아닙니다.";
    private static final String PRODUCT_NOT_FOUND = "상품을 찾을 수 없습니다.";
    private static final String CATEGORY_NOT_FOUND = "카테고리를 찾을 수 없습니다.";
    private static final String FAILED_DELETE_IMG = "이미지 삭제에 실패했습니다.";

    public ProductResponse create(final Product product, final Long categoryId, final Long storeId) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        product.initCategory(findCategory);
        product.initStore(findStore);
        Product savedProduct = productRepository.save(product);

        initBasicImg(savedProduct);

        return ProductResponse.of(savedProduct);
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
        log.info("oldPath is [{}]", oldPath);
        String uuid = UUID.randomUUID().toString();
        String path = GetPathByEnvironment.getPath("update") + uuid;

        dropOldImgMetaFromDB(findProduct.getProductImg());

        ProductImg newProductImg = ProductImg.builder()
                .name(uuid)
                .path(path)
                .contentType(newFile.getContentType())
                .product(findProduct)
                .build();

        ProductImg savedProductImg = productImgRepository.save(newProductImg);
        findProduct.initProductImg(savedProductImg);

        newFile.transferTo(new File(path));

        boolean result = deleteTargetFile(oldPath);
        if (!result) {
            throw new FailedDeleteImgException(FAILED_DELETE_IMG);
        }

        return ProductImgResponse.of(savedProductImg);
    }

    public void validateStoreOwner(final Long storeId, final Long targetId) {
        if (!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(OWNER_MISMATCHED);
        }
    }

    public boolean deleteTargetFile(final String oldPath) {

        if (!oldPath.contains("basic.jpg")) {
            File file = new File(oldPath);
            return file.delete();
        }

        return true;
    }

    public void initBasicImg(final Product product) throws BasicImgInitFailedException {
        File target = new File(GetPathByEnvironment.getPath("basic"));

        ProductImg basicProductImg = ProductImg.builder()
                .name(target.getName())
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .path(target.getPath())
                .product(product)
                .build();

        ProductImg savedImg = productImgRepository.save(basicProductImg);
        product.initProductImg(savedImg);
    }

    public void dropOldImgMetaFromDB(final ProductImg productImg) {
        productImgRepository.delete(productImg);
    }
}
