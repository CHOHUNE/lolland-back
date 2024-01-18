package com.example.lollandback.board.review.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetReviewDto {
    private List<ReviewDto> reviewList;
    private Long totalReviews;

    public GetReviewDto () {
    }

    public GetReviewDto(List<ReviewDto> reviewList, Long totalReviews) {
        this.reviewList = reviewList;
        this.totalReviews = totalReviews;
    }
}
