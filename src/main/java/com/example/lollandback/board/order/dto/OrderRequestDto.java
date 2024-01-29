package com.example.lollandback.board.order.dto;

import com.example.lollandback.board.product.dto.ProductAndOptionDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private String orderId;
    private String orderName;
    private Double totalPrice;
    private String member_login_id;
    private String customerName;
    private String customerEmail;
    private String customerMobilePhone;
    private String receiver;
    private String address;
    private String postalCode;
    private String requirement;
    private List<ProductAndOptionDto> ProductAndOptionDto;
}
