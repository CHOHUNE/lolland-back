package com.example.lollandback.board.review.dto;

import lombok.Data;

@Data
public class ReviewUpdateDto {
    private Long review_id;
    private String review_content;
    private Integer rate;
}
