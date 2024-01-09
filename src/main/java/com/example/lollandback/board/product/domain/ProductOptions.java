package com.example.lollandback.board.product.domain;

import lombok.Data;

@Data
public class ProductOptions {
    private Long option_id;
    private Long product_id;
    private String option_name;
    private Long stock;
}
