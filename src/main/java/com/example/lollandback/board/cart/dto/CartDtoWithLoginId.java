package com.example.lollandback.board.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDtoWithLoginId {
    private List<CartDto> cartDtoList;
    private String member_login_id;

    public CartDtoWithLoginId() {

    }

    public CartDtoWithLoginId(List<CartDto> cartDtoList, String member_login_id) {
        this.cartDtoList = cartDtoList;
        this.member_login_id=member_login_id;
    }
}
