package com.example.lollandback.board.cart.service;

import com.example.lollandback.board.cart.dto.CartDto;
import com.example.lollandback.board.cart.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public List<CartDto> fetchCartByMember(Long member_id) {
        List<CartDto> cartDtoList = cartMapper.fetchCartByMemberId(member_id);

        cartDtoList.forEach(cartDto -> {
            List<String> modifiedMainImgUri = cartDto.getMain_img_uri().stream()
                    .map(uri -> "prefix" + uri) //TODO: prefix 수정
                    .collect(Collectors.toList());
            cartDto.setMain_img_uri(modifiedMainImgUri);
        });

        return cartDtoList;
    }
}
