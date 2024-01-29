package com.example.lollandback.board.product.dto;

import com.example.lollandback.board.product.domain.ProductImg;
import lombok.Data;

import java.util.List;

@Data
public class MainProductDto {
    private Long product_id;
    private String product_name;
    private Double product_price;
    private String company_name;
    private String product_content;
    private List<ProductImg> productImgs;

}
