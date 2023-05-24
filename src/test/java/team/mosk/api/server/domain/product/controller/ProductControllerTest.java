package team.mosk.api.server.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.product.dto.*;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.vo.Selling;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.domain.product.util.GivenProduct;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.util.WithAuthUser;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("windows")
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;
    @MockBean
    ProductReadService productReadService;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * create test start
     */
    @Test
    @DisplayName("상품 생성 요청 JSON의 내용에 따라 상품을 생성한다.")
    @WithAuthUser
    void create() throws Exception {
        when(productService.create(any(), any(), any(), any(), any())).thenReturn(ProductResponse.of(GivenProduct.toEntityWithProductCount()));

        CreateProductRequest createRequest = CreateProductRequest.builder()
                .name(GivenProduct.PRODUCT_NAME)
                .description(GivenProduct.PRODUCT_DESCRIPTION)
                .price(GivenProduct.PRODUCT_PRICE)
                .categoryId(1L)
                .build();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/v1/products")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(createRequest.getName()))
                .andExpect(jsonPath("$.data.description").value(createRequest.getDescription()))
                .andExpect(jsonPath("$.data.price").value(createRequest.getPrice()))
                .andExpect(jsonPath("$.data.selling").value(Selling.SELLING.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 상품 생성 요청 JSON 중 이름이 누락되었다면 예외를 발생시킨다.")
    @WithAuthUser
    void createHasThrowsExceptionCauseNameIsBlack() throws Exception{
        CreateProductRequest createRequest = CreateProductRequest.builder()
                .description(GivenProduct.PRODUCT_DESCRIPTION)
                .price(GivenProduct.PRODUCT_PRICE)
                .categoryId(1L)
                .build();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/v1/products")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 상품 생성 요청 JSON 중 가격이 누락되었다면 예외를 발생시킨다.")
    @WithAuthUser
    void createHasThrowsExceptionCausePriceIsNull() throws Exception{
        CreateProductRequest createRequest = CreateProductRequest.builder()
                .name(GivenProduct.PRODUCT_NAME)
                .description(GivenProduct.PRODUCT_DESCRIPTION)
                .categoryId(1L)
                .build();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/v1/products")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("가격 설정은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 상품 생성 요청 JSON 중 카테고리 ID가 누락되었다면 예외를 발생시킨다.")
    @WithAuthUser
    void createHasThrowsExceptionCauseCategoryIdIsNull() throws Exception{
        CreateProductRequest createRequest = CreateProductRequest.builder()
                .name(GivenProduct.PRODUCT_NAME)
                .description(GivenProduct.PRODUCT_DESCRIPTION)
                .price(GivenProduct.PRODUCT_PRICE)
                .build();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/v1/products")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("카테고리 아이디는 필수입니다."))
                .andDo(print());
    }

    /**
     * create test end
     */

    /**
     * update test start
     */

    @Test
    @DisplayName("상품 수정 요청 JSON의 내용에 따라 상품 정보를 수정한다.")
    @WithAuthUser
    void update() throws Exception {
        ProductResponse updateResponse = ProductResponse.of(GivenProduct.toEntityForUpdateTest());

        when(productService.update(any(), any(), any(), any())).thenReturn(updateResponse);

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .productId(1L)
                .name(GivenProduct.MODIFIED_PRODUCT_NAME)
                .description(GivenProduct.MODIFIED_PRODUCT_DESCRIPTION)
                .price(GivenProduct.MODIFIED_PRODUCT_PRICE)
                .build();

        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/products")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(updateRequest.getProductId()))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()))
                .andExpect(jsonPath("$.data.description").value(updateRequest.getDescription()))
                .andExpect(jsonPath("$.data.price").value(updateRequest.getPrice()))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정 요청 JSON의 내용 중 상품 ID가 누락되었을 시 예외를 발생시킨다.")
    @WithAuthUser
    void updateHasThrowsExceptionCauseProductIdIsNull() throws Exception {
        ProductResponse updateResponse = ProductResponse.of(GivenProduct.toEntityForUpdateTest());

        when(productService.update(any(), any(), any(), any())).thenReturn(updateResponse);

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name(GivenProduct.MODIFIED_PRODUCT_NAME)
                .description(GivenProduct.MODIFIED_PRODUCT_DESCRIPTION)
                .price(GivenProduct.MODIFIED_PRODUCT_PRICE)
                .build();

        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/products")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 아이디는 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정 요청 JSON의 내용 중 상품 이름이 누락되었을 시 예외를 발생시킨다.")
    @WithAuthUser
    void updateHasThrowsExceptionCauseNameIsBlank() throws Exception {
        ProductResponse updateResponse = ProductResponse.of(GivenProduct.toEntityForUpdateTest());

        when(productService.update(any(), any(), any(), any())).thenReturn(updateResponse);

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .productId(1L)
                .description(GivenProduct.MODIFIED_PRODUCT_DESCRIPTION)
                .price(GivenProduct.MODIFIED_PRODUCT_PRICE)
                .build();

        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/products")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정 요청 JSON의 내용 중 상품 가격이 누락되었을 시 예외를 발생시킨다.")
    @WithAuthUser
    void updateHasThrowsExceptionCausePriceIsNull() throws Exception {
        ProductResponse updateResponse = ProductResponse.of(GivenProduct.toEntityForUpdateTest());

        when(productService.update(any(), any(), any(), any())).thenReturn(updateResponse);

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .productId(1L)
                .name(GivenProduct.MODIFIED_PRODUCT_NAME)
                .description(GivenProduct.MODIFIED_PRODUCT_DESCRIPTION)
                .build();

        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/products")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("가격 설정은 필수입니다."))
                .andDo(print());
    }

    /**
     * update test end
     */

    /**
     * delete test start
     */
    @Test
    @DisplayName("URL에 해당하는 ID를 가진 상품을 삭제한다.")
    @WithAuthUser
    void remove() throws Exception {
        doNothing().when(productService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * delete test start end
     */

    /**
     * change selling status test start
     */
    @Test
    @DisplayName("해당하는 ID를 가진 상품의 현재 판매 상태를 전환한다.")
    @WithAuthUser
    void changeSellingStatus() throws Exception {
        SellingStatusRequest request = SellingStatusRequest.builder()
                .productId(1L)
                .selling(Selling.SOLDOUT)
                .build();

        doNothing().when(productService).changeSellingStatus(any(), any());

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/products/status")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("판매 상태 변경 요청 JSON 중 상품 ID가 누락되었을 시 예외를 발생시킨다.")
    @WithAuthUser
    void changeSellingStatusHasThrowsExceptionCauseProductIdIsNull() throws Exception {
        SellingStatusRequest request = SellingStatusRequest.builder()
                .selling(Selling.SOLDOUT)
                .build();

        doNothing().when(productService).changeSellingStatus(any(), any());

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/products/status")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 아이디는 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("판매 상태 변경 요청 JSON 중 상태가 누락되었을 시 예외를 발생시킨다.")
    @WithAuthUser
    void changeSellingStatusHasThrowsExceptionCauseStatusIdIsNull() throws Exception {
        SellingStatusRequest request = SellingStatusRequest.builder()
                .productId(1L)
                .build();

        doNothing().when(productService).changeSellingStatus(any(), any());

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/products/status")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상태는 필수입니다."))
                .andDo(print());
    }

    /**
     * change selling status test end;
     */

    /**
     * ReadService Methods
     */


}
