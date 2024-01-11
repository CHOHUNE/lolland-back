package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.ProductOptions;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface ProductOptionMapper {
    @Insert("""
            INSERT INTO productoptions (product_id, option_name)
            VALUES (#{product_id}, #{option_name})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "option_id")
    int insert(ProductOptions productOptions);
}

