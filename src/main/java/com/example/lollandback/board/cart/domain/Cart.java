package com.example.lollandback.board.cart.domain;

import com.example.lollandback.board.product.dto.OptionPurchaseDto;
import lombok.Data;

@Data
public class Cart {
    private Long cart_id;
    private Long member_id;
    private Long product_id;
    private Long option_id;
    private Integer quantity;

    public Cart() {

    }

    public Cart(Long member_id, Long product_id, OptionPurchaseDto optionDto) {
        this.member_id = member_id;
        this.product_id = product_id;
        this.option_id = optionDto.getOption_id();
        this.quantity = optionDto.getQuantity();
    }
}
