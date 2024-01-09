package com.example.lollandback.board.product.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long product_id;
    private Long category_id;
    private Long subcategory_id;
    private Long company_id;
    private String product_name;
    private String product_content;
    private Double product_price;
    private Long total_stock;
    private Double average_rate;
    private LocalDateTime product_reg_time;
}
