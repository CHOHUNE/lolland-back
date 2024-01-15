package com.example.lollandback.board.like.mapper;

import com.example.lollandback.board.like.domain.ProductLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
