package com.example.lollandback.board.order.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductOrder {
    private Long order_id;
    private Long product_id;
    private Long member_id;
    private Long order_status_id;
    private String order_name;
    private Integer post_code;
    private String address;
    private String detail_address;
    private Double price;
    private Double quantity;
    private String reciever_name;
    private String reciever_phone;
    private LocalDateTime order_reg_time;
}
