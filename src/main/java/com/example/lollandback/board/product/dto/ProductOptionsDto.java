package com.example.lollandback.board.product.dto;

import lombok.Data;

@Data
public class ProductOptionsDto {
    private Long option_id;
    private Long product_id;
    private String option_name;
    private Long stock;
}
