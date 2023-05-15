package team.mosk.api.server.domain.option.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.option.dto.CreateOptionGroupRequest;
import team.mosk.api.server.domain.option.dto.OptionGroupResponse;
import team.mosk.api.server.domain.option.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class OptionGroupController {

    @PostMapping("/opgroups")
    @ResponseStatus(CREATED)
    public ApiResponse<OptionGroupResponse> create(@Validated @RequestBody CreateOptionGroupRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails details) {
        return null;
    }

    @PutMapping("/opgroups")
    @ResponseStatus(OK)
    public ApiResponse<OptionGroupResponse> update(@Validated @RequestBody UpdateOptionGroupRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails details) {
        return null;
    }

    @DeleteMapping("/opgroups/{optionGroupId}")
    @ResponseStatus(NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long optionGroupId,
                                    @AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.of(NO_CONTENT, null);
    }
}
