package com.example.lollandback.board.event.dto;

import com.example.lollandback.board.product.domain.ProductImg;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class EventProduct {
    private Long product_id;
    private Long category_id;
    private Long subcategory_id;
    private Long company_id;
    private Long member_id;
    private String category_name;
    private String subcategory_name;
    private String product_name;
    private String product_content;
    private Double product_price;
    private String company_name;
    private Long total_stock;
    private Double average_rate;
    private LocalDateTime product_reg_time;
    private List<ProductImg> mainImgs;
}
