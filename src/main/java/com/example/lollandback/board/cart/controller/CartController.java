package com.example.lollandback.board.cart.controller;

import com.example.lollandback.board.cart.domain.Cart;
import com.example.lollandback.board.cart.dto.CartDto;
import com.example.lollandback.board.cart.dto.CartDtoWithLoginId;
import com.example.lollandback.board.cart.dto.ReceiveCartDto;
import com.example.lollandback.board.cart.service.CartService;
import com.example.lollandback.board.product.dto.OptionPurchaseDto;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/fetch")
    public ResponseEntity<CartDtoWithLoginId> fetchCart(@SessionAttribute("login") Member login) {
        try {
            String member_login_id = login.getMember_login_id();
            Long memberId = login.getId();
            if(member_login_id != null) {
                CartDtoWithLoginId dto = cartService.fetchCartByMember(memberId, member_login_id);
                return ResponseEntity.ok().body(dto);
            } else {
                System.out.println("member_login_id가 존재하지 않음 ");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            System.out.println("카트 가져오는 도중 에러 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity addProductToCart(@SessionAttribute("login") Member login, @RequestBody ReceiveCartDto cartDto) {
        Long memberId = login.getId();
        Long productId = cartDto.getProduct_id();

        List<OptionPurchaseDto> selectedOptionList = cartDto.getSelectedOptionList();

        List<Cart> cartList = new ArrayList<>();

        for (OptionPurchaseDto optionDto : selectedOptionList) {
            Cart cart = new Cart(memberId, productId, optionDto);
            cartList.add(cart);
        }

        try {
            cartService.addProductToCart(cartList);
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
    @DeleteMapping("/delete/all")
    public ResponseEntity deleteAllCart(@SessionAttribute("login") Member login) {
        String member_login_id = login.getMember_login_id();
        try {
            cartService.deleteAllByMember(member_login_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("멤버 "+ member_login_id + "의 카트 비우기 도중 에러 발생" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
