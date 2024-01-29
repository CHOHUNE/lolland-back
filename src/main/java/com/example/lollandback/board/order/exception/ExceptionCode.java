package com.example.lollandback.board.order.exception;

public enum ExceptionCode {
    NOT_FOR_SALE("구매할 수 없는 상품입니다"),
    OUT_OF_STOCK("재고 부족"),
    PAYMENT_MISMATCH("금액 불일치"),
    ORDER_NOT_FOUND("존재하지 않는 주문입니다"),
    ORDER_CANCELED("이미 취소된 주문입니다"),
    PAYMENT_DONE("결제가 이미 완료되었습니다");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
