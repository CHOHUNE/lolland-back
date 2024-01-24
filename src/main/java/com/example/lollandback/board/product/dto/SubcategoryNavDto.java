package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.Category;
import com.example.lollandback.board.product.domain.Company;
import lombok.Data;

import java.util.List;

@Data
public class SubcategoryNavDto {
    private List<Category> categories;
    private List<Company> companies;
    private String category_name;
    private String subcategory_name;

    public SubcategoryNavDto() {

    }

    public SubcategoryNavDto(List<Category> categories, List<Company> companies, String category_name, String subcategory_name) {
        this.categories = categories;
        this.companies = companies;
        this.category_name = category_name;
        this.subcategory_name = subcategory_name;
    }
}
