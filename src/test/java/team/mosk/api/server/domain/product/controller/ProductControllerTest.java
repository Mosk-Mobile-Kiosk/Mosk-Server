package team.mosk.api.server.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.mosk.api.server.domain.product.dto.*;
import team.mosk.api.server.domain.product.model.persist.ProductImg;
import team.mosk.api.server.domain.product.model.vo.Selling;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.domain.product.util.GivenProduct;
import team.mosk.api.server.domain.store.WithAuthUser;
import team.mosk.api.server.domain.store.WithAuthUserSecurityContextFactory;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    @DisplayName("상품 삭제")
    @WithAuthUser
    void remove() throws Exception {
        doNothing().when(productService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 ID로 상품 조회")
    @Transactional(readOnly = true)
    @WithAuthUser
    void findByProductId() throws Exception {
        ProductResponse response = ProductResponse.of(GivenProduct.toEntityWithProductCount());

        when(productReadService.findByProductId(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("상점 ID로 전체 상품 페이징 조회")
    @Transactional(readOnly = true)
    void findAllWithPaging() throws Exception {
        when(productReadService.findAllWithPaging(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/public/products?storeId=1&size=10&page=0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 이름과 상점 ID로 해당 상점의 카테고리 상품 조회")
    @Transactional(readOnly = true)
    void findAllByCategoryNameEachStore() throws Exception {
        ProductSearch request
                = ProductSearch.builder()
                .categoryName("테스트 카테고리")
                .storeId(1L)
                .build();

        List<ProductResponse> list = new ArrayList<>();
        list.add(ProductResponse.of(GivenProduct.toEntityWithProductCount()));

        when(productReadService.findAllByCategoryNameEachStore(any())).thenReturn(list);

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(get("/api/v1/public/products/category")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("판매 상태 변경")
    @WithAuthUser
    void changeSellingStatus() throws Exception {
        SellingStatusRequest request = SellingStatusRequest.builder()
                .productId(1L)
                .selling(Selling.SOLDOUT)
                .build();

        doNothing().when(productService).changeSellingStatus(any(),any());

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/products/selling")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * files
     */

    @Test
    @DisplayName("상품 ID로 이미지 조회")
    @Transactional(readOnly = true)
    void findImgByProductId() throws Exception {
        ProductImg productImg = ProductImg.builder()
                .id(1L)
                .product(GivenProduct.toEntityWithProductCount())
                .path("C:\\Users\\Student\\Desktop\\study\\KakaoTalk_20230413_100820237.jpg")
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .name("KakaoTalk_20230413_100820237")
                .build();


        when(productReadService.findImgByProductId(any())).thenReturn(ProductImgResponse.of(productImg));

        mockMvc.perform(get("/api/v1/products/img/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 이미지 업데이트")
    @WithAuthUser
    void updateImg() throws Exception{
        ProductImg updateImg = ProductImg.builder()
                .id(1L)
                .product(GivenProduct.toEntityWithProductCount())
                .path("C:\\Users\\Student\\Desktop\\study\\img.jpg")
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .name("img")
                .build();

        when(productService.updateImg(any(), any(), any())).thenReturn(ProductImgResponse.of(updateImg));

        MockMultipartFile file =
                new MockMultipartFile("newImg", "test.jpg", "image/jpeg", "test image content".getBytes());

        Store store = Store.builder()
                .id(1L)
                .email("test@test.test")
                .build();

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/v1/products/img")
                .file(file)
                .param("productId", "1")
                .with(user(CustomUserDetails.of(store))))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
