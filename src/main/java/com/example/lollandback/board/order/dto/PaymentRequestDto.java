package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String paymentKey;
    private String orderId;
    private Long amount;
}
