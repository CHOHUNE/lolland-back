package com.example.lollandback.board.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentCancelDto {
    private String mId;
    private String version;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String currency;
    private String method;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private List<Cancels> cancels;
    private String type;
    private String country;
    private String failure;
    private Integer totalAmount;
    private Integer balanceAmount;
    private Integer vat;
}
