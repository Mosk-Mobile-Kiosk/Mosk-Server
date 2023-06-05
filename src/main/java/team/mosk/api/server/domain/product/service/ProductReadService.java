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
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductReadService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final ResourceLoader resourceLoader;

    public ProductResponse findByProductId(final Long id) {
        return productRepository.findByProductId(id);
    }

    public List<ProductResponse> findByKeyword(final String keyword, final Long storeId) {
        return productRepository.findByKeyword(keyword, storeId);
    }

    public ProductImgResponse findImgByProductId(final Long productId) throws Exception{
        ProductImg findImg = productImgRepository.findImgByProductId(productId)
                .orElseThrow(() -> new ProductImgNotFoundException(ErrorCode.PRODUCT_IMG_NOT_FOUND));

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
}
