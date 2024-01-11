package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Product product;
    private String company_name;
    private Long category_id;
    private String category_name;
    private Long subcategory_id;
    private String subcategory_name;
    private List<String> mainImgUrls;
}
