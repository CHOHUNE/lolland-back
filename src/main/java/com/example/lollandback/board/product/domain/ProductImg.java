package com.example.lollandback.board.product.domain;

import lombok.Data;

@Data
public class ProductImg {
    private Long main_img_id;
    private Long product_id;
    private Long option_id;
    private String main_img_uri;
}
