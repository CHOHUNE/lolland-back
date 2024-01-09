package com.example.lollandback.board.product.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long product_id;
    private Long category_id;
    private Long subCategory_id;
    private Long company_id;
    private String name;
    private Double price;
    private Long total_stock;
    private Double average_rate;
    private String product_content;
    private LocalDateTime product_regTime;
}
