package team.mosk.api.server.domain.order.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    INIT("주문생성"),               //초기값
    CANCELED("주문취소"),           //환불
    PAYMENT_COMPLETED("결제완료"),  //결제완료
    PAYMENT_FAILED("결제실패"),     //카드정지, 잔액부족등등.. (API Error)
    COMPLETED("처리완료");          //상품 손님에게 나감 (사장님들이 처리)

    private final String text;
}
