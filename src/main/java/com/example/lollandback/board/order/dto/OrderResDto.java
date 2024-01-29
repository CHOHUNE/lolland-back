package com.example.lollandback.board.order.dto;

import com.example.lollandback.board.order.domain.Order;
import lombok.Data;

@Data
public class OrderResDto {
    private Long id;
    private String order_nano_id;
    private String order_name;
    private String customer_name;
    private String email;
    private String phone;
    private Double amount;

    public OrderResDto() {}

    public OrderResDto(Long id, OrderRequestDto dto) {
        this.id = id;
        this.order_nano_id = dto.getOrderId();
        this.order_name = dto.getOrderName();
        this.customer_name = dto.getCustomerName();
        this.email = dto.getCustomerEmail();
        this.phone = dto.getCustomerMobilePhone();
        this.amount = dto.getTotalPrice();
    }
}
