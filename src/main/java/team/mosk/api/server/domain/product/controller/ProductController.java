package team.mosk.api.server.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.mosk.api.server.domain.product.dto.*;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    private final ProductReadService productReadService;

    @PostMapping("/products")
    @ResponseStatus(CREATED)
    public ApiResponse<ProductResponse> create(@Validated @RequestBody CreateProductRequest request,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.of(CREATED, productService.create(request.toEntity(), request.getCategoryId(), userDetails.getId()));
    }

    @PutMapping("/products")
    @ResponseStatus(OK)
    public ApiResponse<ProductResponse> update(@Validated @RequestBody UpdateProductRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(productService.update(request, userDetails.getId()));
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

    @GetMapping("/public/products/category")
    @ResponseStatus(OK)
    public ApiResponse<List<ProductResponse>> findAllByCategoryNameEachStore(@ModelAttribute ProductSearchFromCategory productSearchFromCategory) {
        return ApiResponse.ok(productReadService.findAllByCategoryIdEachStore(productSearchFromCategory));
    }

    @GetMapping("/public/products/all")
    @ResponseStatus(OK)
    public ApiResponse<Page<ProductResponse>> findAllWithPaging(@RequestParam(name = "storeId") Long storeId,
                                                                   @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.ok(productReadService.findAllWithPaging(storeId, pageable));
    }

    @GetMapping("/public/products")
    @ResponseStatus(OK)
    public ApiResponse<ProductResponse> findByProductId(@ModelAttribute ProductSearch productSearch) {
        return ApiResponse.ok(productReadService.findByProductIdAndStoreId(productSearch));
    }

    @GetMapping("/public/products/keywords")
    @ResponseStatus(OK)
    public ApiResponse<List<ProductResponse>> findProductsHasKeyword(@RequestParam(name = "storeId") Long storeId,
                                                                        @RequestParam(name = "keyword") String keyword) {
        return ApiResponse.ok(productReadService.findProductsHasKeyword(storeId, keyword));
    }


    /**
     * files
     */

    @GetMapping("/public/products/img/{productId}")
    @ResponseStatus(OK)
    public ResponseEntity<byte[]> findImgByProductId(@PathVariable Long productId) throws IOException {
        ProductImgResponse response = productReadService.findImgByProductId(productId);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(response.getContentType()))
                .body(response.getFile());
    }

    @PutMapping("/products/img")
    @ResponseStatus(OK)
    public ResponseEntity<byte[]> updateImg(@RequestParam(name = "newImg") MultipartFile newFile,
                                            @RequestParam(name = "productId") Long productId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        ProductImgResponse response = productService.updateImg(newFile, productId, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(response.getContentType()))
                .body(response.getFile());
    }
}
