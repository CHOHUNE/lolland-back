package com.example.lollandback.board.product.dto;

import lombok.Data;

@Data
public class ProductAndOptionDto {
    private Long product_id;
    private Long option_id;
    private Integer quantity;
}
