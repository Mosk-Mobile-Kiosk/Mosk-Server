package team.mosk.api.server.domain.category.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.category.model.persist.Category;
import team.mosk.api.server.domain.category.model.persist.CategoryRepository;
import team.mosk.api.server.domain.category.util.GivenCategory;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.error.StoreNotFoundException;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.domain.store.service.StoreService;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"windows", "dev"})
@Transactional
public class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    StoreService storeService;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    CategoryReadService categoryReadService;

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
    @DisplayName("카테고리 저장")
    @WithAuthUser
    void create() {
        CategoryResponse categoryResponse = categoryService.create(GivenCategory.toEntity(), store.getId());
        Category toEntity = GivenCategory.toEntity();

        assertThat(categoryResponse.getName()).isEqualTo(toEntity.getName());
    }

    @Test
    @DisplayName("카테고리 업데이트")
    @WithAuthUser
    void update() {
        CategoryResponse categoryResponse = categoryService.create(GivenCategory.toEntity(), store.getId());
        UpdateCategoryRequest updateRequest = UpdateCategoryRequest.builder()
                .categoryId(categoryResponse.getId())
                .name("newName")
                .build();

        CategoryResponse updateResponse
                = categoryService.update(updateRequest, store.getId());

        assertThat(updateRequest.getName()).isEqualTo(updateResponse.getName());
    }

    @Test
    @DisplayName("카테고리 삭제")
    @WithAuthUser
    void delete() {
        CategoryResponse createResponse = categoryService.create(GivenCategory.toEntity(), store.getId());

        categoryService.delete(createResponse.getId(), store.getId());
        Optional<Category> findCategory = categoryRepository.findById(createResponse.getId());

        assertThat(findCategory.isPresent()).isFalse();
    }

    @Test
    @DisplayName("상점ID 기반 카테고리 목록 조회")
    @Transactional(readOnly = true)
    @WithAuthUser
    void findAllByStoreId() {
        categoryService.create(GivenCategory.toEntity(), store.getId());

        List<CategoryResponse> categories
                = categoryReadService.findAllByStoreId(store.getId());

        assertThat(categories.size()).isEqualTo(1);
    }
}
