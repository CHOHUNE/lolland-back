package com.example.lollandback.board.order.domain;

import com.example.lollandback.board.order.dto.OrderRequestDto;
import lombok.Data;

@Data
public class OrderCustomerDetails {
    private Long id;
    private Long order_id;
    private String receiver;
    private String email;
    private String phone;
    private String address;
    private String postalCode;
    private String requirement;

    public OrderCustomerDetails() {}
    public OrderCustomerDetails(Long order_id, OrderRequestDto dto) {
        this.order_id = order_id;
        this.receiver = dto.getReceiver();
        this.email = dto.getCustomerEmail();
        this.phone = dto.getCustomerMobilePhone();
        this.address = dto.getAddress();
        this.postalCode = dto.getPostalCode();
        this.requirement = dto.getRequirement();
    }
}
