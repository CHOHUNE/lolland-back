package com.example.lollandback.board.cart.mapper;

import com.example.lollandback.board.cart.domain.Cart;
import com.example.lollandback.board.cart.dto.CartDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {

    @Select("""
    SELECT c.cart_id, c.product_id, cat.category_name, sub.subcategory_name, 
    p.product_name, p.product_price, com.company_name, img.main_img_uri, c.count 
    FROM cart c
    INNER JOIN product p ON c.product_id = p.product_id
    INNER JOIN category cat ON p.category_id = cat.category_id
    INNER JOIN subcategory sub ON p.subcategory_id = sub.subcategory_id
    INNER JOIN company com ON p.company_id = com.company_id
    INNER JOIN productimg img ON p.product_id = img.product_id
    WHERE c.member_id = #{memberId}
    """)
    List<CartDto> fetchCartByMemberLoginId(Long memberId);

    @Insert("""
        INSERT INTO cart (member_id, product_id, count)
        VALUES (#{cart.member_id}, #{cart.product_id}, #{cart.count})
    """)
    void addProductToCart(Cart cart);

    @Delete("""
        DELETE FROM cart
        WHERE cart_id = #{cartId}
    """)
    void deleteByCartId(Long cartId);

//    @Delete("""
//        DELETE FROM cart
//        WHERE cart_id IN
//        <foreach collection="cartIds" open="(" separator="," close=")">
//            #{cartId}
//        </foreach>
//    """)
//    void deleteSelected(List<Long> cartIds); //TODO: IN이 인식이 안된다

    @Delete("""
        DELETE FROM cart
        WHERE member_id = #{memberId}
    """)
    void deleteAllByMember(Long memberId);
}
