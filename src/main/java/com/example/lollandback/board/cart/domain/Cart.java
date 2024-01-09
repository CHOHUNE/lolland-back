package com.example.lollandback.board.cart.domain;

import lombok.Data;

@Data
public class Cart {
    private Long cart_id;
    private Long member_id;
    private Long product_id;
    private Integer count;
}
