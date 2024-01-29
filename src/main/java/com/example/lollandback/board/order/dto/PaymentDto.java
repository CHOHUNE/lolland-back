package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Long amount;
    private String orderName;
    private String orderMemberLoginId;
    private Long orderId;
    private String customerName;
    private String customerEmail;
    private String customerMobilePhone;
    private String receiver;
    private String address;
    private String postalCode;
    private String requirement;
}
