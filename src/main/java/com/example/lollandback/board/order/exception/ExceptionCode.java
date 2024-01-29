package com.example.lollandback.board.order.exception;

public enum ExceptionCode {
    NOT_FOR_SALE("구매할 수 없는 상품입니다"),
    OUT_OF_STOCK("재고 부족"),
    PAYMENT_MISMATCH("금액 불일치");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
