package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.ProductOptions;
import com.example.lollandback.board.product.dto.ProductOptionsDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductOptionMapper {
    @Insert("""
            INSERT INTO productoptions (product_id, option_name)
            VALUES (#{product_id}, #{option_name})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "option_id")
    int insert(ProductOptions productOptions);

    @Select("""
            SELECT *
            FROM productoptions
            WHERE product_id = #{product_id}
            """)
    List<ProductOptionsDto> getOptionByProductId(Integer producId);

    @Delete("""
            DELETE FROM productoptions
            WHERE product_id = #{product_id}
            """)
    void deleteByOption(Long productId);


    @Update("""
            UPDATE productoptions
            SET
                option_name = #{option_name}
            WHERE product_id = #{product_id}
            """)
    int update(List<ProductOptionsDto> options);
}

