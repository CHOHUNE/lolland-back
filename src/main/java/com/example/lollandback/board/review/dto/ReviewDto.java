package com.example.lollandback.board.review.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long review_id;
    private Long product_id;
    private Long member_id;
    private Long member_login_id;
    private String review_content;
    private Integer rate;
}
