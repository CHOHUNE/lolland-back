package com.example.lollandback.board.like.mapper;

import com.example.lollandback.board.like.domain.ProductLike;
import com.example.lollandback.board.like.dto.ProductLikeDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductLikeMapper {
    @Delete("""
            DELETE FROM productlike
            WHERE product_id = #{product_id}
                AND member_id = #{member_id}
            """)
    int delete(ProductLike product_id);

    @Insert("""
            INSERT INTO productlike (product_id, member_id)
            VALUES (#{product_id}, #{member_id})
            """)
    int insert(ProductLike productLike);

    @Select("""
            SELECT *
            FROM productlike
            WHERE product_id = #{product_id}
                AND member_id = #{member_id}
            """)
    ProductLike selectByProductIdAndMemberId(Long product_id, Long member_id);

    @Select("""
            SELECT 
                pl.like_id, 
                pl.member_id, 
                pl.product_id, 
                p.product_name, 
                p.product_price,
                c.company_name,
                CONCAT(#{urlPrefix}, 'lolland/product/productMainImg/', pl.product_id, '/', SUBSTRING_INDEX(GROUP_CONCAT(pi.main_img_uri ORDER BY pi.main_img_id ASC), ',', 1)) AS main_img_uri
            FROM productlike pl
            INNER JOIN product p ON pl.product_id = p.product_id
            LEFT JOIN productimg pi ON p.product_id = pi.product_id
            JOIN company c ON p.company_id = c.company_id
            WHERE pl.member_id = #{member_id}
            GROUP BY pl.like_id, pl.member_id, pl.product_id, p.product_name, p.product_price
            """)
    List<ProductLikeDto> selectById(Long member_id, String urlPrefix);


    @Delete("""
            DELETE FROM productlike
            WHERE like_id = #{like_id}
            """)
    void deleteById(Long productLikeId);

    @Delete("""
            DELETE FROM productlike
            WHERE product_id = #{product_id}
            """)
    void deleteByProductId(Long product_id);

    @Delete("""
            DELETE FROM productlike
            WHERE member_id = #{member_id}
            """)
    void deleteLikeByMemberId(Long member_id);
}
