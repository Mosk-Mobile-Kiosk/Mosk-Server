package team.mosk.api.server.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.domain.product.dto.CreateProductRequest;
import team.mosk.api.server.domain.product.dto.ProductResponse;
import team.mosk.api.server.domain.product.dto.UpdateProductRequest;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.domain.product.util.GivenProduct;
import team.mosk.api.server.domain.store.WithAuthUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;
    @MockBean
    ProductReadService productReadService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("상품 생성")
    @WithAuthUser
    void create() throws Exception {
        when(productService.create(any(), any(), any())).thenReturn(ProductResponse.of(GivenProduct.toEntityWithProductCount()));

        CreateProductRequest createRequest = CreateProductRequest.builder()
                .name("a")
                .description("de")
                .price(1L)
                .categoryId(1L)
                .build();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/api/v1/products")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 수정")
    @WithAuthUser
    void update() throws Exception {
        ProductResponse updateResponse = ProductResponse.of(GivenProduct.toEntityForUpdateTest());

        when(productService.update(any(),any())).thenReturn(updateResponse);

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .productId(1L)
                .name("a")
                .description("de")
                .price(10L)
                .build();

        String requestJSON = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/v1/products")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
