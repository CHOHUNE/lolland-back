package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class OrderedProductDto {
    private Long product_id;
    private Long option_id;
    private String product_name;
    private String option_name;
    private String quantity;
    private String total_price;
}
