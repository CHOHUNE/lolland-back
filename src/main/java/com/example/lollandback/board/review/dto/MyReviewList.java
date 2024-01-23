package com.example.lollandback.board.review.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyReviewList {
    private Long review_id;
    private Long product_id;
    private String product_name;
    private String review_content;
    private Integer rate;
    private LocalDateTime review_reg_time;
}
