package team.mosk.api.server.domain.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.category.dto.CreateCategoryRequest;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.category.dto.CategoryResponse;
import team.mosk.api.server.domain.category.service.CategoryReadService;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.global.aop.ValidSubscribe;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@ValidSubscribe
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryReadService categoryReadService;

    @PostMapping("/categories")
    @ResponseStatus(CREATED)
    public ApiResponse<CategoryResponse> create(@Validated @RequestBody CreateCategoryRequest request,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.of(CREATED, categoryService.create(request.toEntity(), userDetails.getId()));
    }

    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long categoryId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        categoryService.delete(categoryId, userDetails.getId());
        return ApiResponse.of(NO_CONTENT, null);
    }

    @PutMapping("/categories")
    @ResponseStatus(OK)
    public ApiResponse<CategoryResponse> update(@Validated @RequestBody UpdateCategoryRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) throws IllegalAccessException {
        return ApiResponse.ok(categoryService.update(request, userDetails.getId()));
    }

    /**
     * ReadService Methods
     */

    @GetMapping("/public/categories/all/{storeId}")
    @ResponseStatus(OK)
    public ApiResponse<List<CategoryResponse>> findAllByStoreId(@PathVariable Long storeId) {
        return ApiResponse.ok(categoryReadService.findAllByStoreId(storeId));
    }

    @GetMapping("/public/categories/{categoryId}")
    @ResponseStatus(OK)
    public ApiResponse<CategoryResponse> findByCategoryId(@PathVariable Long categoryId) {
        return ApiResponse.ok(categoryReadService.findByCategoryId(categoryId));
    }
}
