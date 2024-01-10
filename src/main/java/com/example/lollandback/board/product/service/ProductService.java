package com.example.lollandback.board.product.service;

import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    public List<CategoryDto> getAllCategories() {
        return productMapper.getAllCategoriesWithSub();
    }

}
