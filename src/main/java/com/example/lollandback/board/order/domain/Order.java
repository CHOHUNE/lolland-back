package com.example.lollandback.board.order.domain;

import com.example.lollandback.board.order.dto.OrderRequestDto;
import lombok.Data;

@Data
public class Order {
    private Long id;
    private String order_nano_id;
    private String order_name;
    private Long member_id;
    private Double total_price;
    private OrderStatus order_status;
    private String recevier;
    private String email;
    private String phone;
    private String address;
    private String postalCode;
    private String requirement;

    public Order() {}

    public Order(Long member_id, OrderRequestDto dto) {
        this.order_nano_id = dto.getOrderId();
        this.order_name = dto.getOrderName();
        this.total_price = dto.getTotalPrice();
        this.member_id = member_id;
        this.order_status = OrderStatus.ORDERING;
        this.recevier = dto.getReceiver();
        this.email = dto.getCustomerEmail();
        this.phone = dto.getCustomerMobilePhone();
        this.address = dto.getAddress();
        this.postalCode = dto.getPostalCode();
        this.requirement = dto.getRequirement();
    }
}
