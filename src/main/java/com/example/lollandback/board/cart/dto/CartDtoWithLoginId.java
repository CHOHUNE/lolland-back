package com.example.lollandback.board.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDtoWithLoginId {
    private List<CartDto> cartDtoList;
    private String member_login_id;
}
