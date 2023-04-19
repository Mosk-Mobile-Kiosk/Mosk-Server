package team.mosk.api.server.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    private final ProductReadService productReadService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> create(@Validated @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request.toEntity(), request.getCategoryId()));
    }

    @PutMapping("/products")
    public ResponseEntity<ProductResponse> update(@Validated @RequestBody UpdateProductRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(productService.update(request, userDetails.getId()));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        productService.delete(productId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> findByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productReadService.findByProductId(productId));
    }

    @GetMapping("/products/all")
    public ResponseEntity<Page<ProductResponse>> findAllWithPaging(@RequestParam(name = "storeName") String storeName,
                                                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productReadService.findAllWithPaging(storeName, pageable));
    }

    @GetMapping("/products/category")
    public ResponseEntity<List<ProductResponse>> findAllByCategoryNameEachStore(@ModelAttribute ProductSearch productSearch) {
        return ResponseEntity.ok(productReadService.findAllByCategoryNameEachStore(productSearch));
    }

    /**
     * files
     */

    @GetMapping("/products/img/{productId}")
    public ResponseEntity<byte[]> findImgByProductId(@PathVariable Long productId){
        ProductImgResponse response = productReadService.findImgByProductId(productId);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(response.getContentType()))
                .body(response.getFile());
    }

    @PutMapping("/products/img")
    public ResponseEntity<byte[]> updateImg(@RequestParam(name = "newImg") MultipartFile newFile,
                                            @RequestParam(name = "productId") Long productId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ProductImgResponse response = productService.updateImg(newFile, productId, userDetails.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(response.getContentType()))
                .body(response.getFile());
    }
}
