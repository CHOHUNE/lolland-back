package com.example.lollandback.board.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long category_id;
    private String category_name;
    private List<SubCategoryDto> subCategory;
}