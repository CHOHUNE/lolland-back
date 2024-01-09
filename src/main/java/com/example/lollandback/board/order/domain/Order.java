package com.example.lollandback.board.order.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long order_id;
    private Long product_id;
    private Long member_id;
    private Long order_status_id;
    private String order_name;
    private Integer postal_code;
    private String address;
    private String detail_address;
    private Double price;
    private Double quantity;
    private String reciever;
    private String phone_number;
    private LocalDateTime order_regTime;
}
