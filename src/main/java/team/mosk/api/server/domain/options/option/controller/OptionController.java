package team.mosk.api.server.domain.options.option.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.options.option.dto.CreateOptionRequest;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.option.dto.UpdateOptionRequest;
import team.mosk.api.server.domain.options.option.service.OptionReadService;
import team.mosk.api.server.domain.options.option.service.OptionService;
import team.mosk.api.server.global.aop.ValidSubscribe;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@ValidSubscribe
public class OptionController {

    private final OptionService optionService;
    private final OptionReadService optionReadService;

    @PostMapping("/options")
    @ResponseStatus(CREATED)
    public ApiResponse<OptionResponse> create(@Validated @RequestBody CreateOptionRequest request,
                                              @AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.of(CREATED, optionService.create(request.toEntity(), request.getOptionGroupId(), details.getId()));
    }

    @PutMapping("/options")
    @ResponseStatus(OK)
    public ApiResponse<OptionResponse> update(@Validated @RequestBody UpdateOptionRequest request,
                                              @AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.ok(optionService.update(request, details.getId()));
    }

    @DeleteMapping("/options/{optionId}")
    @ResponseStatus(NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long optionId,
                                    @AuthenticationPrincipal CustomUserDetails details) {
        optionService.delete(optionId, details.getId());
        return ApiResponse.of(NO_CONTENT, null);
    }

    /**
     * ReadService
     */

    @GetMapping("/public/options/{optionId}")
    @ResponseStatus(OK)
    public ApiResponse<OptionResponse> findByOptionId(@PathVariable Long optionId) {
        return ApiResponse.ok(optionReadService.findByOptionId(optionId));
    }
}
