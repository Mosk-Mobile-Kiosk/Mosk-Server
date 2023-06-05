package team.mosk.api.server.domain.product.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.product.dto.ProductImgResponse;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.SellingStatusRequest;
import team.mosk.api.server.domain.product.dto.UpdateProductRequest;
import team.mosk.api.server.domain.product.error.ProductImgNotFoundException;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    private final StoreRepository storeRepository;
    private final ResourceLoader resourceLoader;
    private final String basicFilePath;
    private final String filePath;

    public ProductService(CategoryRepository categoryRepository,
                          ProductRepository productRepository,
                          ProductImgRepository productImgRepository,
                          StoreRepository storeRepository,
                          ResourceLoader resourceLoader,
                          @Value("${basicFilePath}") String basicFilePath,
                          @Value("${filePath}") String filePath) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productImgRepository = productImgRepository;
        this.storeRepository = storeRepository;
        this.resourceLoader = resourceLoader;
        this.basicFilePath = basicFilePath;
        this.filePath = filePath;
    }


    public ProductResponse create(final Product product,
                                  final String encodedImg,
                                  final String imgType,
                                  final Long categoryId,
                                  final Long storeId) throws Exception {

        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        product.initCategory(findCategory);
        product.initStore(findStore);
        Product savedProduct = productRepository.save(product);

        // img save
        if(StringUtils.hasText(encodedImg) && StringUtils.hasText(imgType)) {
            //초기 이미지 입력이 존재할 경우
            saveParamImg(encodedImg, imgType, savedProduct);

            return ProductResponse.of(savedProduct);
        } else {
            initBasicImg(savedProduct);

            return ProductResponse.of(savedProduct);
        }
    }

    public ProductResponse update(final UpdateProductRequest request, final String encodedImg, final String imgType, final Long storeId) throws Exception {
        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        if(StringUtils.hasText(encodedImg) && StringUtils.hasText(imgType)) {
            ProductImg findImg = productImgRepository.findImgByProductId(findProduct.getId())
                    .orElseThrow(() -> new ProductImgNotFoundException(ErrorCode.PRODUCT_IMG_NOT_FOUND));

            final String oldPath = findImg.getPath();

            productImgRepository.delete(findImg);

            if(!oldPath.contains("basic.jpg")) {
                File target = new File(oldPath);
                target.delete();
            }

            saveParamImg(encodedImg, imgType, findProduct);
        }

        findProduct.update(request);
        return ProductResponse.of(findProduct);
    }

    public void delete(final Long productId, final Long storeId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        productRepository.delete(findProduct);
    }

    public void changeSellingStatus(final SellingStatusRequest request, final Long storeId) {
        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        validateStoreOwner(findProduct.getStore().getId(), storeId);

        if (findProduct.getSelling() != request.getSelling()) {
            findProduct.changeSellingStatus(request.getSelling());
        }
    }

    /**
     * utils
     */

    private void validateStoreOwner(final Long storeId, final Long targetId) {
        if (!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(ErrorCode.OWNER_INFO_MISMATCHED);
        }
    }

    private File saveImgToFile(final byte[] imgBytes, final String fileName) throws Exception {
        String fullPath = filePath + fileName;
        Files.write(Paths.get(fullPath), imgBytes);

        return new File(fullPath);
    }

    private void initBasicImg(final Product product) {
        ProductImg productImg = ProductImg.builder()
                .name("basic.jpg")
                .path(basicFilePath)
                .product(product)
                .build();

        ProductImg savedProductImg
                = productImgRepository.save(productImg);

        product.initProductImg(savedProductImg);
    }

    private void saveParamImg(String encodedImg, String imgType, Product product) throws Exception {

        byte[] decodedImg = Base64.getDecoder().decode(encodedImg.substring(encodedImg.indexOf(",") + 1));
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);

        if(imgType.contains(".")) {
            newFileName += imgType;
        } else {
            newFileName = newFileName + "." + imgType;
        }

        File file = saveImgToFile(decodedImg, newFileName);
        ProductImg productImg = ProductImg.builder()
                .name(newFileName)
                .path(file.getPath())
                .product(product)
                .build();

        ProductImg savedImg = productImgRepository.save(productImg);
        product.initProductImg(savedImg);
    }
}
