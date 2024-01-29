package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class UpdateStockDto {
    private Long product_id;
    private Long option_id;
    private Integer quantity;
}
