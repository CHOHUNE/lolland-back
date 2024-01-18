package com.example.lollandback.board.cart.dto;

import com.example.lollandback.board.product.dto.OptionPurchaseDto;
import lombok.Data;

import java.util.List;

@Data
public class ReceiveCartDto {
    private Long member_id;
    private Long product_id;
    private List<OptionPurchaseDto> selectedOptionList;
}
