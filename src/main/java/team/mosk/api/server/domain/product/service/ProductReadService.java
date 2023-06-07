package team.mosk.api.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductReadService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final ResourceLoader resourceLoader;

    private static final String PRODUCT_NOT_FOUND = "상품을 찾을 수 없습니다.";
    private static final String PRODUCT_IMG_NOT_FOUND = "이미지를 찾을 수 없습니다.";

    public ProductResponse findByProductId(final Long id) {
        return productRepository.findByProductId(id);
    }

    public List<ProductResponse> findByKeyword(final String keyword, final Long storeId) {
        return productRepository.findByKeyword(keyword, storeId);
    }

    public ProductImgResponse findImgByProductId(final Long productId) throws Exception{
        ProductImg findImg = productImgRepository.findImgByProductId(productId)
                .orElseThrow(() -> new ProductImgNotFoundException(PRODUCT_IMG_NOT_FOUND));

        if(findImg.getName().contains("basic.jpg")) {
            Resource resource = resourceLoader.getResource(findImg.getPath());
            File file = resource.getFile();
            byte[] bytes = Files.readAllBytes(file.toPath());

            return ProductImgResponse.of(findImg, bytes);
        } else {
            byte[] bytes = Files.readAllBytes(new File(findImg.getPath()).toPath());
            return ProductImgResponse.of(findImg, bytes);
        }
    }

    public List<ProductResponse> findByStoreId(Long storeId) {
        List<Product> products = productRepository.findByStoreId(storeId);

        return products.stream()
                .map(product -> ProductResponse.ofWithCategory(product))
                .toList();
    }
}
