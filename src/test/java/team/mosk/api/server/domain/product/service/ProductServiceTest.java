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
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
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
    void create() {
        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, category.getId(), store.getId());

        assertThat(productResponse.getName()).isEqualTo(product.getName());
        assertThat(productResponse.getDescription()).isEqualTo(product.getDescription());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("상품 수정")
    @WithAuthUser
    void update() {

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, category.getId(), store.getId());

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

    @Test
    @DisplayName("판매 상태 변경")
    @WithAuthUser
    void changeSellingStatus() {

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, category.getId(), store.getId());

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
     * files
     */
    @Test
    @DisplayName("상품 이미지 업데이트")
    @WithAuthUser
    void updateProductImg(@TempDir Path temp) throws Exception {

        Path testPath = temp.resolve("image.jpg");
        Files.write(testPath, "test".getBytes());

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, category.getId(), store.getId());

        InputStream stream = new FileInputStream(testPath.toFile());
        MockMultipartFile multipartFile
                = new MockMultipartFile("file", testPath.getFileName().toString(), "image/jpeg", stream);

        ProductImgResponse productImgResponse = productService.updateImg(multipartFile, productResponse.getId(), store.getId());

        Product findProduct = productRepository.findById(productResponse.getId())
                .orElseThrow(() -> new ProductNotFoundException("updateProductImg TEST HAS THROW EXCEPTION"));

        ProductImg findProductImg = findProduct.getProductImg();

        File testFile = new File(findProductImg.getPath());
        System.out.println(testFile.getPath());
        byte[] testFileByteArraysByPath
                = Files.readAllBytes(new File(testFile.getPath()).toPath());

        assertThat(productImgResponse.getFile()).isEqualTo(testFileByteArraysByPath);
    }

    /**
     * ReadService methods
     */

    @Test
    @DisplayName("상품 ID로 상품 조회")
    void findByProductId() {

        Product product = GivenProduct.toEntity();
        ProductResponse productResponse = productService.create(product, category.getId(), store.getId());

        ProductSearch search = ProductSearch.builder()
                .productId(1L)
                .storeId(1L)
                .build();

        ProductResponse findProduct = productReadService.findByProductIdAndStoreId(search);

        assertThat(productResponse.getId()).isEqualTo(findProduct.getId());
        assertThat(productResponse.getName()).isEqualTo(findProduct.getName());
        assertThat(productResponse.getDescription()).isEqualTo(findProduct.getDescription());
        assertThat(productResponse.getPrice()).isEqualTo(findProduct.getPrice());
        assertThat(productResponse.getSelling()).isEqualTo(findProduct.getSelling());
    }

    @Test
    @DisplayName("상점 ID로 전체 상품 페이징 조회")
    void findAllWithPaging() {

        Product product = GivenProduct.toEntity();
        productService.create(product, category.getId(), store.getId());

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductResponse> response = productReadService.findAllWithPaging(store.getId(), pageRequest);

        List<ProductResponse> content = response.getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("카테고리명, 상점 ID로 해당 카테고리 상품 조회")
    void findAllByCategoryNameEachStore() {

        Product product = GivenProduct.toEntity();
        productService.create(product, category.getId(), store.getId());

        ProductSearchFromCategory search = ProductSearchFromCategory.builder()
                .storeId(1L)
                .categoryId(1L)
                .build();

        List<ProductResponse> list = productReadService.findAllByCategoryIdEachStore(search);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("키워드로 상점 상품 검색")
    void findProductsHasKeyword() {
        Product product1 = Product.builder()
                .name("초코우유")
                .build();
        productService.create(product1, category.getId(), store.getId());

        Product product2 = Product.builder()
                .name("초코라떼")
                .build();
        productService.create(product2, category.getId(), store.getId());

        String keyword = "초코";

        List<ProductResponse> products
                = productReadService.findProductsHasKeyword(store.getId(), keyword);

        assertThat(products.size()).isEqualTo(2);
        assertThat(hasText(products.get(0).getName(), keyword)).isTrue();
        assertThat(hasText(products.get(1).getName(), keyword)).isTrue();
    }

    /**
     * ReadService files
     */

    @Test
    @DisplayName("상품 ID로 상품 이미지 조회")
    void findImgByProductId() throws Exception {

        Product product = GivenProduct.toEntity();
        ProductResponse savedProduct = productService.create(product, category.getId(), store.getId());

        ProductImgResponse response = productReadService.findImgByProductId(savedProduct.getId());

        Product findProduct = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new ProductNotFoundException("ERROR"));
        ProductImg productImg = findProduct.getProductImg();
        byte[] findByteArrays = Files.readAllBytes(new File(productImg.getPath()).toPath());

        assertThat(response.getFile()).isEqualTo(findByteArrays);
    }
}
