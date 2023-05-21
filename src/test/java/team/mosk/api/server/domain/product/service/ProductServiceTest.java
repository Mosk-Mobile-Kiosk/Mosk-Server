package team.mosk.api.server.domain.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.domain.category.util.GivenCategory;
import team.mosk.api.server.domain.product.dto.*;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.persist.ProductImgRepository;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.product.model.vo.Selling;
import team.mosk.api.server.domain.product.util.GivenProduct;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.exception.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.store.service.StoreService;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static team.mosk.api.server.domain.product.util.TextValidator.*;

@SpringBootTest
@ActiveProfiles({"windows", "dev"})
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
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StoreService storeService;
    @Autowired
    StoreRepository storeRepository;

    static Store store;
    static Category category;

    static String encodedImg = "";
    static String imgType = "";

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

        CategoryResponse categoryResponse = categoryService.create(GivenCategory.toEntity(), store.getId());

        category = categoryRepository.findById(categoryResponse.getId())
                .orElseThrow(() -> new CategoryNotFoundException("error"));
    }

    @Test
    @DisplayName("상품 생성")
    @WithAuthUser
    void create() throws Exception {
        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, encodedImg, imgType, category.getId(), store.getId());

        assertThat(productResponse.getName()).isEqualTo(product.getName());
        assertThat(productResponse.getDescription()).isEqualTo(product.getDescription());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("상품 수정")
    @WithAuthUser
    void update() throws Exception {

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, encodedImg, imgType, category.getId(), store.getId());

        UpdateProductRequest request = UpdateProductRequest.builder()
                .productId(productResponse.getId())
                .name("newName")
                .price(30L)
                .description("newDes")
                .build();

        ProductResponse updateResponse = productService.update(request, encodedImg,imgType, store.getId());

        assertThat(updateResponse.getName()).isEqualTo(request.getName());
        assertThat(updateResponse.getDescription()).isEqualTo(request.getDescription());
        assertThat(updateResponse.getPrice()).isEqualTo(request.getPrice());
    }

    @Test
    @DisplayName("판매 상태 변경")
    @WithAuthUser
    void changeSellingStatus() throws Exception {

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, encodedImg, imgType, category.getId(), store.getId());

        SellingStatusRequest request = SellingStatusRequest.builder()
                .productId(productResponse.getId())
                .selling(Selling.SOLDOUT)
                .build();

        productService.changeSellingStatus(request, store.getId());

        Product findProduct = productRepository.findById(productResponse.getId())
                .orElseThrow(() -> new ProductNotFoundException("[changeSellingStatus] TEST HAS THROW EXCEPTION"));

        assertThat(findProduct.getSelling()).isEqualTo(Selling.SOLDOUT);
    }


    /**
     * ReadService methods
     */

}
