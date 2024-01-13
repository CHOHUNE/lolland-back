package com.example.lollandback.board.product.dto;

import lombok.Data;

@Data
public class ProductUpdateDto {
    private Long product_id;
    private String product_name;
    private Double product_price;
    private String product_content;
    private Long total_stock;
    private String company_name;
    private Long company_id;
    private Long category_id;
    private Long subcategory_id;
}
