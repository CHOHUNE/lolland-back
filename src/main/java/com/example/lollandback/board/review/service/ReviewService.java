package com.example.lollandback.board.review.service;

import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.board.review.dto.ReviewDto;
import com.example.lollandback.board.review.dto.ReviewUpdateDto;
import com.example.lollandback.board.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public List<ReviewDto> getAllReviewsByProduct(Long product_id) {
        return reviewMapper.getAllReviewsByProduct(product_id);
    }

    public List<ReviewDto> getAllReviewsByMember(Long member_id) {
        return reviewMapper.getAllReviewsByMember(member_id);
    }

    public void addNewReview(Review review) {
        reviewMapper.addNewReview(review);
    }

    @Transactional
    public void updateReview(ReviewUpdateDto updatedReview) {
        reviewMapper.updateReview(updatedReview);
    }

    @Transactional
    public void deleteReviewById(Long reviewId) {
        reviewMapper.deleteReviewById(reviewId);
    }

    @Transactional
    public void deleteSelectedReviews(List<Long> reviewIds) {
        reviewMapper.deleteSelectedReviews(reviewIds);
    }

    @Transactional
    public void deleteAllReviewsByMember(Long memberId) {
        reviewMapper.deleteReviewByMember(memberId);
    }
}
