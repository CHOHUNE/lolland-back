package com.example.lollandback.board.cart.controller;

import com.example.lollandback.board.cart.dto.CartDto;
import com.example.lollandback.board.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/fetch")
    List<CartDto> fetchCart(@RequestParam Long member_id) {
        cartService.fetchCartByMember(member_id);

        return null;
    }
}
