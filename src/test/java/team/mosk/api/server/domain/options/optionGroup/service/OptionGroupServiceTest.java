package team.mosk.api.server.domain.options.optionGroup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroupRepository;
import team.mosk.api.server.domain.options.optionGroup.util.GivenOptionGroup;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.domain.product.model.vo.Selling;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.store.service.StoreService;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("mac")
@Transactional
public class OptionGroupServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductReadService productReadService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    StoreService storeService;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OptionGroupService optionGroupService;
    @Autowired
    OptionGroupRepository optionGroupRepository;

    static Store store;

    static Category category;
    static Product product;
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
                () -> new StoreNotFoundException("error"));

        Category newCategory = Category.builder()
                .name("category")
                .build();

        CategoryResponse categoryResponse = categoryService.create(newCategory, store.getId());

        category = categoryRepository.findById(categoryResponse.getId())
                .orElseThrow(() -> new CategoryNotFoundException("error"));

        Product newProduct = Product.builder()
                .name("product")
                .description("des")
                .price(1000L)
                .selling(Selling.SELLING)
                .build();

        ProductResponse productResponse = productService.create(newProduct, category.getId(), store.getId());

        product = productRepository.findById(productResponse.getId())
                .orElseThrow(() -> new ProductNotFoundException("error"));
    }

    @Test
    @DisplayName("전달받은 엔티티 객체를 저장하고 응답 객체를 반환한다.")
    void create() {
        OptionGroupResponse response
                = optionGroupService.create(GivenOptionGroup.toEntity(), product.getId(), store.getId());

        System.out.println("response = " + response);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo(GivenOptionGroup.GROUP_NAME);
        assertThat(response.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("전달받은 요청 객체를 기반하여 엔티티 객체를 수정한다.")
    void update() {

        OptionGroupResponse savedResponse
                = optionGroupService.create(GivenOptionGroup.toEntity(), product.getId(), store.getId());

        UpdateOptionGroupRequest request = UpdateOptionGroupRequest.builder()
                .groupId(savedResponse.getId())
                .name("NEW NAME")
                .build();

        OptionGroupResponse response = optionGroupService.update(request, store.getId());

        assertThat(response.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("전달받은 옵션 아이디를 기반하여 옵션 엔티티를 조회하고 삭제한다.")
    void delete() {
        OptionGroupResponse response
                = optionGroupService.create(GivenOptionGroup.toEntity(), product.getId(), store.getId());

        final Long id = response.getId();

        optionGroupService.delete(id, store.getId());

        Optional<OptionGroup> optional
                = optionGroupRepository.findById(id);

        assertThat(optional.isPresent()).isFalse();
    }
}
