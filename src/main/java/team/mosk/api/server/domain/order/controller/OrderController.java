package team.mosk.api.server.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.mosk.api.server.domain.order.dto.CreateOrderRequest;
import team.mosk.api.server.domain.order.dto.OrderResponse;
import team.mosk.api.server.domain.order.service.OrderService;
import team.mosk.api.server.global.aop.ValidSubscribe;
import team.mosk.api.server.global.common.ApiResponse;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/public/{storeId}/orders")
    public ApiResponse<OrderResponse> create(
            @PathVariable Long storeId,
            @Validated @RequestBody CreateOrderRequest request
    ) {
        LocalDateTime now = LocalDateTime.now();
        return ApiResponse.of(HttpStatus.CREATED, orderService.createOrder(storeId, request, now));
    }

    @DeleteMapping("/orders/{tossOrderId}")
    public ApiResponse<Void> cancel(
            @PathVariable Long tossOrderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        orderService.cancel(customUserDetails.getId(), tossOrderId);
        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }

    @PutMapping("/orders/{orderId}/completion")
    public ApiResponse<Void> complete(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        orderService.orderCompleted(customUserDetails.getId(), orderId);
        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }


}
