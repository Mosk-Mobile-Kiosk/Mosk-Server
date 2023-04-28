package team.mosk.api.server.domain.product.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.category.util.GivenCategory;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.UpdateProductRequest;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.product.util.GivenProduct;
import team.mosk.api.server.domain.store.WithAuthUser;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.exception.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.store.service.StoreService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductReadService productReadService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImgRepository productImgRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StoreService storeService;
    @Autowired
    StoreRepository storeRepository;

    static Store store;
    @BeforeEach
    void init() {
        Store newStore = Store.builder()
                .email("test@test.test")
                .password("12345")
                .storeName("TestStore")
                .ownerName("TestOwner")
                .call("02-000-0000")
                .crn("000-00-00000")
                .address("TestAddress")
                .build();

        StoreResponse response = storeService.create(newStore);

        store = storeRepository.findById(response.getId()).orElseThrow(
                () -> new StoreNotFoundException("error")
        );
    }

    @Test
    @DisplayName("상품 생성")
    @WithAuthUser
    void create() {
        categoryRepository.save(GivenCategory.toEntity());

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, 1L, 1L);

        assertThat(productResponse.getName()).isEqualTo(product.getName());
        assertThat(productResponse.getDescription()).isEqualTo(product.getDescription());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("상품 수정")
    @WithAuthUser
    void update() {
        categoryRepository.save(GivenCategory.toEntity());

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, 1L, 1L);

        UpdateProductRequest request = UpdateProductRequest.builder()
                .productId(productResponse.getId())
                .name("newName")
                .price(30L)
                .description("newDes")
                .build();

        ProductResponse updateResponse = productService.update(request, store.getId());

        assertThat(updateResponse.getName()).isEqualTo(request.getName());
        assertThat(updateResponse.getDescription()).isEqualTo(request.getDescription());
        assertThat(updateResponse.getPrice()).isEqualTo(request.getPrice());
    }
}
