package team.mosk.api.server.domain.subscribe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import team.mosk.api.server.domain.subscribe.dto.SubscribeHistoryResponse;
import team.mosk.api.server.domain.subscribe.dto.SubscribeResponse;
import team.mosk.api.server.domain.subscribe.service.SubscribeReadService;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubscribeController {

    private final SubscribeReadService subscribeReadService;

    @GetMapping("/subscribes")
    @ResponseStatus(OK)
    public ApiResponse<List<SubscribeHistoryResponse>> findAllByStoreId(@AuthenticationPrincipal CustomUserDetails details) {
        return ApiResponse.ok(subscribeReadService.findAllByStoreId(details.getId()));
    }
}
