package com.example.lollandback.board.product.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCategory {
    void deleteByCategory(Long productId);
}
