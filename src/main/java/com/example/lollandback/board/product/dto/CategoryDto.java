package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long category_id;
    private String category_name;
    private List<SubCategoryDto> subCategory;

    public CategoryDto() {

    }

    public CategoryDto(Category category, List<SubCategoryDto> subCategory) {
        this.category_id = category.getCategory_id();
        this.category_name = category.getCategory_name();
        this.subCategory = subCategory;
    }
}
