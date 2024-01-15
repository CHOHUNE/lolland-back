package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.ProductOptions;
import com.example.lollandback.board.product.dto.ProductOptionsDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductOptionMapper {
    @Insert("""
            INSERT INTO productoptions (product_id, option_name, stock)
            VALUES (#{product_id}, #{option_name}, #{stock})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "option_id")
    int insert(ProductOptionsDto productOptions);

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


    @Insert("""
            INSERT INTO productoptions (option_name)
            VALUES (#{optoon_name})
            """)
    boolean insertOptions(ProductOptionsDto productOptionsDto);


    @Update("""
            UPDATE productoptions
            SET
                option_name = #{option_name},
                stock = #{stock}
            WHERE option_id = #{option_id}
            """)
    boolean updateOptions(ProductOptionsDto productOptionsDto);
}

