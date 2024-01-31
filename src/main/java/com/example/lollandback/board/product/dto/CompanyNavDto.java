package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.dto.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class CompanyNavDto {
    //회사명
    private String company_name;
    //회사 상품의 평점 평균
    private Double avg_rate;
    //회사 상품의 총 리뷰
    private Integer total_reviews;
//    회사가 파는 대분류
//        해당 대분류의 소분류
    private List<CategoryDto> categories;

    public CompanyNavDto() {

    }

    public CompanyNavDto(String company_name, Double avg_rate, Integer total_reviews, List<CategoryDto> categories) {
        this.company_name = company_name;
        this.avg_rate = avg_rate;
        this.total_reviews = total_reviews;
        this.categories = categories;
    }
}


