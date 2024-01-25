package com.example.lollandback.board.review.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long review_id;
    private Long product_id;
    private Long member_id;
    private String file_url;
    private String member_login_id;
    private String review_content;
    private LocalDateTime review_reg_time;
    private Integer rate;
}
