package com.example.lollandback.board.like.dto;

import lombok.Data;

@Data
public class ProductLikeDto {
    private Long like_id;
    private Long member_id;
    private Long product_id;
    private String product_name;
    private Double product_price;
    private String main_img_uri;
    private String company_name;
}
