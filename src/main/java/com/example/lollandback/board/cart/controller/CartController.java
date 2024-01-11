package com.example.lollandback.board.cart.controller;

import com.example.lollandback.board.cart.domain.Cart;
import com.example.lollandback.board.cart.dto.CartDto;
import com.example.lollandback.board.cart.dto.CartDtoWithLoginId;
import com.example.lollandback.board.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/fetch")
    CartDtoWithLoginId fetchCart(@RequestParam Long member_id) {
        return cartService.fetchCartByMember(member_id);
    }

    @PostMapping("/add")
    public ResponseEntity addProductToCart(@RequestBody Cart cart) {
        //member_id, product_id, count(fe에서 *Long*으로 넘겨주기)
        try {
            cartService.addProductToCart(cart);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("상품 카트에 추가 중 에러 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //개별 카트 상품 삭제
    @DeleteMapping("/delete")
    public ResponseEntity deleteCart(@RequestParam Long cart_id) {
        try {
            cartService.deleteByCartId(cart_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("카트 상품 개별 삭제 도중 에러 발생" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //선택된 카트 상품 삭제
    @DeleteMapping("/delete/selected")
    public ResponseEntity deleteSelected(@RequestBody List<Long> cart_ids) {
        try {
            cartService.deleteSelected(cart_ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("선택된 카트 상품 삭제 도중 에러 발생" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 멤버의 모든 카트 삭제
    @DeleteMapping("/delete/{member_id}")
    public ResponseEntity deleteAllCart(@PathVariable Long member_id) {
        try {
            cartService.deleteAllByMember(member_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("멤버 "+ member_id + "의 카트 비우기 도중 에러 발생" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
