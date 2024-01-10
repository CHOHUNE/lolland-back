package com.example.lollandback.board.cart.dto;

import lombok.Data;

@Data
public class CartDto {
    private Long cart_id;
    private Long member_id;
    private Long product_id;
    private String member_login_id;
    private String category_name;
    private String subcategory_name;
    private String product_name;
    private String company_name;
    private String main_img_url;
    private Double product_price;
    private Integer count;
}
