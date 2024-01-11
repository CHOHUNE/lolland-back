package com.example.lollandback.board.cart.mapper;

import com.example.lollandback.board.cart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {

    @Select("""
    SELECT c.cart_id, c.member_id, c.product_id, m.member_login_id, cat.category_name, sub.subcategory_name, 
    p.product_name, com.company_name, c.count 
    FROM cart c 
    INNER JOIN member m ON c.member_id = m.member_id
    INNER JOIN product p ON c.product_id = p.product_id
    INNER JOIN category cat ON p.category_id = cat.category_id
    INNER JOIN subcategory sub ON p.subcategory_id = sub.subcategory_id
    INNER JOIN company com ON p.company_id = com.company_id
    INNER JOIN 
    WHERE member_id = #{member_id}
    """)
    List<CartDto> fetchCartByMemberId(Long member_id);
}
