package com.example.lollandback.board.cart.mapper;

import com.example.lollandback.board.cart.domain.Cart;
import com.example.lollandback.board.cart.dto.CartDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Select("""
        SELECT 
            c.cart_id, 
            c.product_id, 
            c.quantity, 
            cat.category_name, 
            sub.subcategory_name, 
            p.product_name, 
            p.product_price, 
            com.company_name, 
            CONCAT(#{urlPrefix}, 'lolland/product/productMainImg/', c.product_id, '/', SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT img.main_img_uri ORDER BY img.main_img_id ASC), ',', 1)) AS main_img_uri, 
            op.option_id,
            op.option_name
        FROM cart c
        INNER JOIN product p ON c.product_id = p.product_id
        INNER JOIN category cat ON p.category_id = cat.category_id
        INNER JOIN subcategory sub ON p.subcategory_id = sub.subcategory_id
        INNER JOIN company com ON p.company_id = com.company_id
        LEFT JOIN productimg img ON p.product_id = img.product_id
        INNER JOIN productoptions op ON op.option_id = c.option_id
        WHERE c.member_id = #{memberId} AND p.product_status = 'none'
        GROUP BY c.product_id, c.option_id
    """)
    List<CartDto> fetchCartByMemberId(Long memberId, String urlPrefix);


    @Select("""
        SELECT *
        FROM cart
        WHERE member_id = #{member_id} AND product_id = #{product_id} AND option_id = #{option_id}
    """)
    Cart getCartByProductAndOption(Long member_id, Long product_id, Long option_id);

    @Insert("""
        INSERT INTO cart (member_id, product_id, option_id, quantity)
        VALUES (#{member_id}, #{product_id},#{option_id} ,#{quantity})
    """)
    void addProductToCart(Cart cart);

    @Update("UPDATE cart SET quantity = #{quantity} WHERE cart_id = #{cart_id}")
    void updateCartQuantity(Cart cart);

    @Delete("""
        DELETE FROM cart
        WHERE cart_id = #{cartId}
    """)
    void deleteByCartId(Long cartId);

    @Delete("""
        <script>
            DELETE FROM cart
            WHERE cart_id IN
            <foreach collection="cartIds" item="cartId" open="(" separator="," close=")">
                #{cartId}
            </foreach>
        </script>
    """)
    void deleteSelected(@Param("cartIds") List<Long> cartIds);

    @Delete("""
        DELETE FROM cart
        WHERE member_id = #{memberId}
    """)
    void deleteAllByMember(Long memberId);

    @Delete("""
        DELETE FROM cart
        WHERE member_id = #{member_id} AND product_id = #{product_id} AND option_id = #{option_id}
    """)
    void deleteCartByMemberAndProductIds(Long member_id, Long product_id, Long option_id);
}
