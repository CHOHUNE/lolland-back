package com.example.lollandback.board.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {
    private Long cart_id;
    private Long product_id;
    private String category_name;
    private String subcategory_name;
    private String product_name;
    private Double product_price;
    private List<String> main_img_uri;
    private String company_name;
    private Integer count;
}
