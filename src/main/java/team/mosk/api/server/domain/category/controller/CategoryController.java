package team.mosk.api.server.domain.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.category.dto.CreateCategoryRequest;
import team.mosk.api.server.domain.category.dto.UpdateCategoryRequest;
import team.mosk.api.server.domain.category.dto.SimpleCategoryResponse;
import team.mosk.api.server.domain.category.service.CategoryReadService;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryReadService categoryReadService;

    @PostMapping("/categories")
    public ResponseEntity<SimpleCategoryResponse> create(@Validated @RequestBody CreateCategoryRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request.toEntity(), userDetails.getId()));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        categoryService.delete(categoryId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/categories")
    public ResponseEntity<SimpleCategoryResponse> update(@Validated @RequestBody UpdateCategoryRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(categoryService.update(request, userDetails.getId()));
    }

    @GetMapping("/categories/{storeName}")
    public ResponseEntity<List<SimpleCategoryResponse>> findAllByStoreName(@PathVariable String storeName) {
        return ResponseEntity.ok(categoryReadService.findAllByStoreName(storeName));
    }
}
