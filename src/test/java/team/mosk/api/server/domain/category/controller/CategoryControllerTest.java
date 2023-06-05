package team.mosk.api.server.domain.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ResponseStatus;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.dto.CreateCategoryRequest;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.mosk.api.server.domain.category.util.GivenCategory.*;


public class CategoryControllerTest extends ControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("전달받은 JSON 으로 카테고리 정보를 저장한다.")
    @WithAuthUser()
    void create() throws Exception {
        CreateCategoryRequest createRequest = CreateCategoryRequest.of(toEntity());
        String requestJSON = objectMapper.writeValueAsString(createRequest);

        when(categoryService.create(any(), any())).thenReturn(CategoryResponse.of(toEntityWithCategoryCount()));

        mockMvc.perform(post("/api/v1/categories").content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(createRequest.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 카테고리 생성 요청 JSON의 name 필드가 비어있을 시 예외를 발생시킨다.")
    @WithAuthUser
    void createHasThrowsExceptionCauseNameIsBlank() throws Exception{
        CreateCategoryRequest createRequest = CreateCategoryRequest.of(toEntityWithBlankName());
        String requestJSON = objectMapper.writeValueAsString(createRequest);

        when(categoryService.create(any(), any())).thenReturn(CategoryResponse.of(toEntityWithCategoryCount()));

        mockMvc.perform(post("/api/v1/categories").content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("이름은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("업데이트 요청 JSON에 따라 해당 ID의 카테고리 정보를 수정한다.")
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
                .andExpect(jsonPath("$.data.id").value(updateRequest.getCategoryId()))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 업데이트 요청 JSON의 ID가 비어있을 시 예외를 발생시킨다.")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @WithAuthUser
    void updateHasThrowsExceptionCauseIdIsNull() throws Exception{
        UpdateCategoryRequest updateRequest = UpdateCategoryRequest.builder()
                .name("New 테스트 카테고리")
                .build();
        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/categories").content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("카테고리 아이디는 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 업데이트 요청 JSON의 이름이 비어있을 시 예외를 발생시킨다.")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @WithAuthUser
    void updateHasThrowsExceptionCauseNameIsBlank() throws Exception{
        UpdateCategoryRequest updateRequest = UpdateCategoryRequest.builder()
                .categoryId(1L)
                .build();
        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/categories").content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$['message']").value("이름은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("URL에 포함된 ID로 카테고리를 삭제한다.")
    @WithAuthUser
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("URL에 포함된 ID로 상점에 속한 모든 카테고리 검색한다.")
    @WithAuthUser
    void findAllByStoreId() throws Exception {
        List<CategoryResponse> list = new ArrayList<>();
        CategoryResponse dummy = CategoryResponse.of(toEntityWithCategoryCount());
        list.add(dummy);

        when(categoryReadService.findAllByStoreId(any())).thenReturn(list);

        mockMvc.perform(get("/api/v1/public/categories/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
