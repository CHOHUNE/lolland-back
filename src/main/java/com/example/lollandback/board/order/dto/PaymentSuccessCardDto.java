package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class PaymentSuccessCardDto {
    private String company;
    private String number;
    private String installmentPlanMonths;
    private String isInterestFree;
    private String approveNo;
    private String useCardPoint;
    private String cardType;
    private String ownerType;
    private String acquireStatus;
    private String receiptUrl;
}
