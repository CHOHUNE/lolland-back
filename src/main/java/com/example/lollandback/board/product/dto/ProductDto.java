package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.domain.ProductDetailsImg;
import com.example.lollandback.board.product.domain.ProductImg;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Product product;
    private Company company;
    private Long product_id; //
    private Long category_id; //
    private Long subcategory_id; //
    private Long company_id;
    private String company_name; //
    private String category_name;
    private String subcategory_name;
    private String product_name; //
    private String product_content; //
    private Double product_price; //
    private Long total_stock;  //
    private Double average_rate;
    private LocalDateTime product_reg_time;
    private List<String> mainImgUrls;
    private List<ProductImg> productImgs;
    private List<ProductDetailsImg> productDetailsImgs;
}
