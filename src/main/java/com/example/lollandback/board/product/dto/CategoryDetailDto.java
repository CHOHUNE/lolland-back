package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDetailDto {
    private Category category;
    private List<SubCategoryDto> subcategories;

    public CategoryDetailDto() {

    }

    public CategoryDetailDto(Category category, List<SubCategoryDto> subcategories) {
        this.category = category;
        this.subcategories = subcategories;
    }
}
