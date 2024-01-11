package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.ProductImg;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMainImg {

    @Insert("""
            INSERT INTO productimg (product_id, main_img_uri)
            VALUES (#{product_id}, #{main_img_uri})
            """)
    void insert(Long product_id, String main_img_uri);

    @Select("""
            SELECT *
            FROM productimg
            WHERE product_id = #{product_id}
            """)
    List<ProductImg> selectByProductId(Long product_id);

    @Select("""
            SELECT main_img_id, main_img_uri
            FROM productimg
            WHERE product_id = #{product_id}
            """)
    List<ProductImg> selectNamesByProductId(Long product_id);

    @Delete("""
            DELETE FROM productimg
            WHERE product_id = #{product_id}
            """)
    void deleteByProductId(Long product_id);
}
