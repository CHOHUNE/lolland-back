package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.ProductDetailsImg;
import com.example.lollandback.board.product.domain.ProductImg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductContentImg {

    @Insert("""
            INSERT INTO productdetailsimg (product_id, sub_img_uri) 
            VALUES (#{product_id}, #{sub_img_uri})
            """)
    void insert(Long product_id, String sub_img_uri);

    @Select("""
            SELECT * 
            FROM productdetailsimg 
            WHERE product_id = #{product_id}
            """)
    List<ProductDetailsImg> selectDetailsByProductId(Long productId);

    @Select("""
            SELECT *
            FROM productdetailsimg
            WHERE details_img_id = #{details_img_id}
            """)
    ProductDetailsImg selectById(Integer details_img_id);


    @Delete("""
            DELETE FROM productdetailsimg
            WHERE details_img_id = #{details_img_id}
            """)
    void deleteById(Integer details_img_id);



    @Select("""
            SELECT details_img_id, sub_img_uri
            FROM productdetailsimg
            WHERE product_id = #{product_id}
            """)
    List<ProductDetailsImg> selectNamesByProductId(Long product_id);

    @Delete("""
            DELETE FROM productdetailsimg
            WHERE product_id = #{product_id}
            """)
    void deleteByProductId(Long productId);
}
