package team.mosk.api.server.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.product.dto.*;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.global.aop.ValidSubscribe;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@ValidSubscribe
public class ProductController {

    private final ProductService productService;
    private final ProductReadService productReadService;

    @PostMapping("/products")
    @ResponseStatus(CREATED)
    public ApiResponse<ProductResponse> create(@Validated @RequestBody CreateProductRequest request,
                              @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ApiResponse.of(CREATED, productService.create(request.toEntity(), request.getEncodedImg(), request.getImgType(), request.getCategoryId(), userDetails.getId()));
    }

    @PutMapping("/products")
    @ResponseStatus(OK)
    public ApiResponse<ProductResponse> update(@Validated @RequestBody UpdateProductRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ApiResponse.ok(productService.update(request, request.getEncodedImg(), request.getImgType(), userDetails.getId()));
    }

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long productId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.delete(productId, userDetails.getId());
        return ApiResponse.of(NO_CONTENT, null);
    }

    @PatchMapping("/products/status")
    @ResponseStatus(OK)
    public ApiResponse<Void> changeSellingStatus(@Validated @RequestBody SellingStatusRequest request,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.changeSellingStatus(request, userDetails.getId());
        return ApiResponse.ok(null);
    }


    /**
     * ReadService Methods
     */

    @GetMapping("/public/products/{productId}")
    @ResponseStatus(OK)
    public ApiResponse<ProductResponse> findByProductId(@PathVariable Long productId) {
        return ApiResponse.ok(productReadService.findByProductId(productId));
    }

    @GetMapping("/public/products/img/{productId}")
    @ResponseStatus(OK)
    public ApiResponse<ProductImgResponse> findImgByProductId(@PathVariable Long productId) throws Exception {
        return ApiResponse.ok(productReadService.findImgByProductId(productId));
    }

    @GetMapping("/public/products")
    @ResponseStatus(OK)
    public ApiResponse<List<ProductResponse>> findByKeyword(@ModelAttribute ProductSearch productSearch) {
        return ApiResponse.ok(productReadService.findByKeyword(productSearch.getProductName(), productSearch.getStoreId()));
    }

    @GetMapping("/products")
    @ResponseStatus(OK)
    public ApiResponse<List<ProductResponse>> findByStoreId(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.ok(productReadService.findByStoreId(customUserDetails.getId()));
    }
}
