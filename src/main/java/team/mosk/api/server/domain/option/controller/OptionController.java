package team.mosk.api.server.domain.option.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.option.dto.CreateOptionRequest;
import team.mosk.api.server.domain.option.dto.OptionResponse;
import team.mosk.api.server.domain.option.dto.UpdateOptionRequest;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class OptionController {

    @PostMapping("/options")
    @ResponseStatus(CREATED)
    public ApiResponse<OptionResponse> create(@Validated @RequestBody CreateOptionRequest request,
                                              @AuthenticationPrincipal CustomUserDetails details) {
        return null;
    }

    @PutMapping("/options")
    @ResponseStatus(OK)
    public ApiResponse<OptionResponse> update(@Validated @RequestBody UpdateOptionRequest request,
                                              @AuthenticationPrincipal CustomUserDetails details) {
        return null;
    }

    @DeleteMapping("/options/{optionId}")
    @ResponseStatus(NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long optionId,
                                    @AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.of(NO_CONTENT, null);
    }
}
