package com.example.lollandback.board.product.domain;

import lombok.Data;

@Data
public class Category {
    private Long category_id;
    private Long subCategory_id;
    private String category_name;
}
