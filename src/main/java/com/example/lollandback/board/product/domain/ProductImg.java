package com.example.lollandback.board.product.domain;

import lombok.Data;

@Data
public class ProductImg {
    private Long product_img_id;
    private Long product_id;
    private Long product_options_id;
    private String product_img_fileUrl;
}
