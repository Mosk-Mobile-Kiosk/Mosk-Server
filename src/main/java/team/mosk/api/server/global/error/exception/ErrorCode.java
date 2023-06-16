package team.mosk.api.server.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * Global
     */
    BAD_REQUEST(400, "BAD REQUEST", "잘못된 요청입니다."),


    /**
     * Category
     */
    CATEGORY_NOT_FOUND(400, "CA001", "해당 카테고리를 찾을 수 없습니다."),


    /**
     * Store
     */
    OWNER_INFO_MISMATCHED(400, "ST001", "상점의 주인이 아닙니다."),
    DUPLICATE_CRN_NUMBER(400, "ST002", "이미 가입된 사업자입니다."),
    DUPLICATE_EMAIL(400, "ST003", "이미 가입된 이메일입니다."),
    QR_CODE_ALREADY_EXISTS(400, "ST004", "이미 QR코드 발급이 완료되었습니다."),
    QR_CODE_NOT_FOUND(400, "ST005", "QR코드가 존재하지 않습니다."),
    STORE_NOT_FOUND(400, "ST006", "상점이 존재하지 않습니다."),

    /**
     * ProductImg
     */
    BASIC_IMG_INIT_FAILED(500, "PRI001", "기본 이미지 삽입 중 문제가 발생했습니다."),
    FAILED_DELETE_IMG(500, "PRI002", "이미지 삭제 중 문제가 발생했습니다."),
    PRODUCT_IMG_NOT_FOUND(400, "PRI003", "상품의 이미지가 존재하지 않습니다."),

    /**
     * Product
     */
    PRODUCT_NOT_FOUND(400, "PR001", "상품이 존재하지 않습니다."),

    /**
     * Option Group
     */
    OPTION_GROUP_NOT_FOUND(400, "OG001", "옵션 그룹이 존재하지 않습니다."),

    /**
     * Option
     */
    OPTION_NOT_FOUND(400, "OP001", "옵션이 존재하지 않습니다."),

    /**
     * Subscribe
     */
    ALREADY_EXPIRED_SUBSCRIBE(400, "SU001", "이미 만료된 구독입니다."),
    SUB_INFO_NOT_FOUND(400, "SU002", "구독 기록이 없습니다."),

    /**
     * Order
     */
    ORDER_CANCEL_DENIED(403, "OR001", "주문 취소가 불가능한 상태입니다."),
    ORDER_ACCESS_DENIED(403, "OR002", "주문에 접근할 수 없는 상태입니다."),
    ORDER_UNCOMPLETED(403, "OR003", "아직 결제가 완료되지 않았습니다."),
    ORDER_NOT_FOUND(400, "OR004", "주문 내역이 존재하지 않습니다."),
    PAYMENT_GATEWAY_UNSTABLE(503, "OR005", "결제 대행사에 문제가 발생했습니다."),
    PAYMENT_GATEWAY_DENIED_PAYMENT(402, "OR006", "결제가 실패했거나 거부되었습니다."),
    PAYMENT_GATEWAY_DENIED_CANCEL(403, "OR007", "결제 취소 요청이 거부되었습니다."),

    /**
     * JWT
     */
    TOKEN_NOT_FOUND(400, "JWT001", "로그인 정보가 존재하지 않습니다.");

    private final int code;
    private final String status;
    private final String message;

    ErrorCode(int code, String status, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
