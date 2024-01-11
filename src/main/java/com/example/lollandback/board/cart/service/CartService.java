package com.example.lollandback.board.cart.service;

import com.example.lollandback.board.cart.domain.Cart;
import com.example.lollandback.board.cart.dto.CartDto;
import com.example.lollandback.board.cart.dto.CartDtoWithLoginId;
import com.example.lollandback.board.cart.mapper.CartMapper;
import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;
    private final MemberMapper memberMapper;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    public CartDtoWithLoginId fetchCartByMember(Long memberId) {
        List<CartDto> cartDtoList = cartMapper.fetchCartByMemberId(memberId);

        cartDtoList.forEach(cartDto -> {
            List<String> modifiedMainImgUri = cartDto.getMain_img_uri().stream()
                    .map(uri -> urlPrefix + "lolland/product/productMainImg/" +  cartDto.getProduct_id() + "/" + uri)
                    .collect(Collectors.toList());
            cartDto.setMain_img_uri(modifiedMainImgUri);
        });

//        memberMapper로 member_login_id 가져오기

//        CartDtoWithLoginId cartDtoWithLoginId = new CartDtoWithLoginId(cartDtoList, member_login_id);
//        return cartDtoWithLoginId;
        return null;
    }

    public void addProductToCart(Cart cart) {
        cartMapper.addProductToCart(cart);
    }

    @Transactional
    public void deleteByCartId(Long cartId) {
        cartMapper.deleteByCartId(cartId);
    }

    @Transactional
    public void deleteSelected(List<Long> cartIds) {
        cartMapper.deleteSelected(cartIds);
    }

    @Transactional
    public void deleteAllByMember(Long memberId) {
        cartMapper.deleteAllByMember(memberId);
    }

}
