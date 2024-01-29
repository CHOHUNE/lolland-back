package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class OrderDto {
    private String status;
    private Long amount;
    private String paymentName;
    private String orderId;
    private String customerEmail;
    private String customerName;
    private String successUrl;
    private String failUrl;

    private String failReason;
    private boolean cancelOrder;
    private String cancelReason;
    private String createdAt;
}
