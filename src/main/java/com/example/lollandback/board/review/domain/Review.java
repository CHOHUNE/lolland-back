package com.example.lollandback.board.review.domain;

import lombok.Data;

@Data
public class Review {
    private Long review_id;
    private Long product_id;
    private Long member_id;
    private String review_content;
    private Integer rate;
}
