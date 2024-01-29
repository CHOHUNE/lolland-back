package com.example.lollandback.board.order.domain;

import com.example.lollandback.board.order.dto.OrderRequestDto;
import com.example.lollandback.board.product.dto.ProductAndOptionDto;
import lombok.Data;

@Data
public class OrderProductDetails {
    private Long id;
    private Long order_id;
    private Long product_id;
    private Long option_id;
    private Integer quantity;
    private Double total_price;

    public OrderProductDetails() {}

    public OrderProductDetails(Long order_id, Double total_price, ProductAndOptionDto dto) {
        this.order_id = order_id;
        this.product_id = dto.getProduct_id();
        this.option_id = dto.getOption_id();
        this.quantity = dto.getQuantity();

    }
}
