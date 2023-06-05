package team.mosk.api.server.domain.options.option.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.IntegrationTestSupport;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.error.CategoryNotFoundException;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.option.dto.UpdateOptionRequest;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.domain.options.option.util.GivenOption;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.error.OptionGroupNotFoundException;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroupRepository;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupService;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;



public class OptionServiceTest extends IntegrationTestSupport {

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
    @Autowired
    OptionService optionService;
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    OptionReadService optionReadService;

    static Store store;
    static Category category;
    static Product product;
    static OptionGroup optionGroup;
    @BeforeEach
    void init() throws Exception {
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
                .products(new ArrayList<>())
                .build();

        CategoryResponse categoryResponse = categoryService.create(newCategory, store.getId());

        category = categoryRepository.findById(categoryResponse.getId())
                .orElseThrow(() -> new CategoryNotFoundException("error"));

        Product newProduct = Product.builder()
                .name("product")
                .description("des")
                .price(1000L)
                .selling(Selling.SELLING)
                .optionGroups(new ArrayList<>())
                .build();

        final String encodedImg = "";
        final String imgType = "";

        ProductResponse productResponse = productService.create(newProduct, encodedImg, imgType, category.getId(), store.getId());

        product = productRepository.findById(productResponse.getId())
                .orElseThrow(() -> new ProductNotFoundException("error"));

        OptionGroup newGroup = OptionGroup.builder()
                .name("GROUP")
                .options(new ArrayList<>())
                .build();

        OptionGroupResponse groupResponse = optionGroupService.create(newGroup, product.getId(), store.getId());

        optionGroup = optionGroupRepository.findById(groupResponse.getId())
                .orElseThrow(() -> new OptionGroupNotFoundException("error"));
    }

    @Test
    @DisplayName("전달받은 엔티티 객체를 저장하고 응답 객체를 반환한다.")
    void create() {
        OptionResponse savedResponse
                = optionService.create(GivenOption.toEntity(), optionGroup.getId(), store.getId());

        Option option = optionRepository.findById(savedResponse.getId())
                .orElseThrow(() -> new OptionGroupNotFoundException("error"));

        assertThat(option.getId()).isEqualTo(savedResponse.getId());
        assertThat(option.getName()).isEqualTo(savedResponse.getName());
        assertThat(option.getPrice()).isEqualTo(savedResponse.getPrice());
        assertThat(option.getOptionGroup().getId()).isEqualTo(optionGroup.getId());
    }

    @Test
    @DisplayName("전달받은 요청을 기반으로 엔티티 객체를 수정하고 응답 객체를 반환한다.")
    void update() {
        OptionResponse savedResponse
                = optionService.create(GivenOption.toEntity(), optionGroup.getId(), store.getId());

        UpdateOptionRequest request = UpdateOptionRequest.builder()
                .optionId(savedResponse.getId())
                .name(GivenOption.MODIFIED_OPTION_NAME)
                .price(GivenOption.MODIFIED_OPTION_PRICE)
                .build();

        OptionResponse updateResponse
                = optionService.update(request, store.getId());

        assertThat(updateResponse.getName()).isEqualTo(GivenOption.MODIFIED_OPTION_NAME);
        assertThat(updateResponse.getPrice()).isEqualTo(GivenOption.MODIFIED_OPTION_PRICE);
    }

    @Test
    @DisplayName("전달받은 옵션 아이디를 기반으로 조회하고 삭제한다.")
    void delete() {
        OptionResponse savedResponse
                = optionService.create(GivenOption.toEntity(), optionGroup.getId(), store.getId());

        final Long id = savedResponse.getId();

        optionService.delete(id, store.getId());
        Optional<Option> findOption = optionRepository.findById(id);

        assertThat(findOption.isPresent()).isFalse();
    }

    @Test
    @DisplayName("전달받은 옵션 아이디를 기반으로 조회하고 응답 객체를 반환한다.")
    void findOptionByOptionId() {
        OptionResponse savedResponse
                = optionService.create(GivenOption.toEntity(), optionGroup.getId(), store.getId());

        final Long id = savedResponse.getId();

        OptionResponse findResponse = optionReadService.findByOptionId(id);

        assertThat(findResponse.getId()).isEqualTo(id);
        assertThat(findResponse.getName()).isEqualTo(GivenOption.OPTION_NAME);
        assertThat(findResponse.getPrice()).isEqualTo(GivenOption.OPTION_PRICE);
    }
}
