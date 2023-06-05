package team.mosk.api.server.domain.options.optionGroup.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.options.optionGroup.dto.CreateOptionGroupRequest;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupReadService;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupService;
import team.mosk.api.server.global.aop.ValidSubscribe;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@ValidSubscribe
public class OptionGroupController {

    private final OptionGroupService optionGroupService;
    private final OptionGroupReadService optionGroupReadService;

    @PostMapping("/optiongroups")
    @ResponseStatus(CREATED)
    public ApiResponse<OptionGroupResponse> create(@Validated @RequestBody CreateOptionGroupRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.of(CREATED, optionGroupService.create(request.toEntity(), request.getProductId(), details.getId()));
    }

    @PutMapping("/optiongroups")
    @ResponseStatus(OK)
    public ApiResponse<OptionGroupResponse> update(@Validated @RequestBody UpdateOptionGroupRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.ok(optionGroupService.update(request, details.getId()));
    }

    @DeleteMapping("/optiongroups/{optionGroupId}")
    @ResponseStatus(NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long optionGroupId,
                                    @AuthenticationPrincipal CustomUserDetails details) {
        optionGroupService.delete(optionGroupId, details.getId());
        return ApiResponse.of(NO_CONTENT, null);
    }


    /**
     * ReadService
     */

    @GetMapping("/public/optiongroups/{optionGroupId}")
    @ResponseStatus(OK)
    public ApiResponse<OptionGroupResponse> findByGroupId(@PathVariable Long optionGroupId) {
        return ApiResponse.ok(optionGroupReadService.findByGroupId(optionGroupId));
    }

    @GetMapping("/public/optiongroups/all/{productId}")
    @ResponseStatus(OK)
    public ApiResponse<List<OptionGroupResponse>> findAllOptionGroupAndOptions(@PathVariable Long productId) {
        return ApiResponse.ok(optionGroupReadService.findAllOptionGroupByProductId(productId));
    }
}
