package com.example.lollandback.board.product.domain;

import lombok.Data;

@Data
public class SubCategory {
    private Long subcategory_id;
    private Long category_id;
    private String subcategory_name;
}
