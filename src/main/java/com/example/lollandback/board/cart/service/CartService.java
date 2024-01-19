package com.example.lollandback.board.cart.service;

import com.example.lollandback.board.cart.domain.Cart;
import com.example.lollandback.board.cart.dto.CartDto;
import com.example.lollandback.board.cart.dto.CartDtoWithLoginId;
import com.example.lollandback.board.cart.mapper.CartMapper;
import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;
    private final MemberMapper memberMapper;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    public CartDtoWithLoginId fetchCartByMember(Long memberId, String memberLoginId) {
        List<CartDto> cartDtoList = cartMapper.fetchCartByMemberId(memberId, urlPrefix);

        CartDtoWithLoginId cartDtoWithLoginId = new CartDtoWithLoginId(cartDtoList, memberLoginId);
        return cartDtoWithLoginId;
    }

    public void addProductToCart(List<Cart> cartList) {
        for(Cart cart : cartList) {
            Cart existingCart = cartMapper.getCartByProductAndOption(cart.getMember_id(), cart.getProduct_id(), cart.getOption_id());
            if (existingCart != null) {
                int newQuantity = existingCart.getQuantity() + cart.getQuantity();
                System.out.println("이미 존재하는 아이템 + 옵션이므로 " + existingCart.getQuantity() + "개에 " + cart.getQuantity() + "추가");
                existingCart.setQuantity(newQuantity);
                System.out.println("existingCart.getQuantity() = " + existingCart.getQuantity());
                cartMapper.updateCartQuantity(existingCart);
            } else {
                cartMapper.addProductToCart(cart);
            }
        }
    }

    @Transactional
    public void deleteByCartId(Long cartId) {
        cartMapper.deleteByCartId(cartId);
    }

    @Transactional
    public void deleteSelected(@Param("cartIds") List<Long> cartIds) {
        cartMapper.deleteSelected(cartIds);
    }

    @Transactional
    public void deleteAllByMember(String memberLoginId) {
        Member member = memberMapper.selectById(memberLoginId);
        Long memberId = member.getId();
        cartMapper.deleteAllByMember(memberId);
    }

}
