package team.mosk.api.server.domain.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.dto.CreateCategoryRequest;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.category.service.CategoryReadService;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.domain.store.WithAuthUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.mosk.api.server.domain.category.util.GivenCategory.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryService categoryService;
    @MockBean
    CategoryReadService categoryReadService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리 정보 저장")
    @WithAuthUser
    void create() throws Exception {
        CreateCategoryRequest createRequest = CreateCategoryRequest.of(toEntity());
        String requestJSON = objectMapper.writeValueAsString(createRequest);

        when(categoryService.create(any(), any())).thenReturn(CategoryResponse.of(toEntityWithCategoryCount()));

        mockMvc.perform(post("/api/v1/categories").content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 정보 업데이트")
    @WithAuthUser
    void update() throws Exception {
        UpdateCategoryRequest updateRequest = UpdateCategoryRequest.builder()
                .categoryId(1L)
                .name("New 테스트 카테고리")
                .build();
        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        when(categoryService.update(any(), any())).thenReturn(CategoryResponse.of(toEntityForUpdateTest()));

        mockMvc.perform(put("/api/v1/categories").content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 삭제")
    @WithAuthUser
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("상점에 속한 모든 카테고리 검색")
    @WithAuthUser
    void findAllByStoreId() throws Exception {
        List<CategoryResponse> list = new ArrayList<>();
        CategoryResponse dummy = CategoryResponse.of(toEntityWithCategoryCount());
        list.add(dummy);

        when(categoryReadService.findAllByStoreId(any())).thenReturn(list);

        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
