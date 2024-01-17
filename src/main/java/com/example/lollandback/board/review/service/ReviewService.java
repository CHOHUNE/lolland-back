package com.example.lollandback.board.review.service;

import com.example.lollandback.board.product.mapper.ProductMapper;
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

    public List<ReviewDto> getAllReviewsByProduct(Long product_id, Integer page, Integer pageSize) {
        Integer offset = page * pageSize;
        return reviewMapper.getAllReviewsByProduct(product_id, offset,  pageSize);
    }

    public List<ReviewDto> getAllReviewsByMember(Long member_id) {
        return reviewMapper.getAllReviewsByMember(member_id);
    }

    public void addNewReview(Review review) {
        reviewMapper.addNewReview(review);
        reviewMapper.updateAvgRateOfProduct(review.getProduct_id());
    }

    @Transactional
    public void updateReview(ReviewUpdateDto updatedReview) {
        reviewMapper.updateReview(updatedReview);
        Long product_id = reviewMapper.getProductIdByReview(updatedReview.getReview_id());
        reviewMapper.updateAvgRateOfProduct(product_id);
    }

    @Transactional
    public void deleteReviewById(Long reviewId) {
        reviewMapper.deleteReviewById(reviewId);
        Long product_id = reviewMapper.getProductIdByReview(reviewId);
        reviewMapper.updateAvgRateOfProduct(product_id);
    }

    @Transactional
    public void deleteSelectedReviews(List<Long> reviewIds) {
        reviewMapper.deleteSelectedReviews(reviewIds);
        List<Long> productIds = reviewMapper.getProductIdsByReview(reviewIds);
        int updatedRows = reviewMapper.updateAvgRateOfProducts(productIds);
        if(updatedRows == productIds.size()) {
            System.out.println("모든 상품 평점 업데이트 완료");
        } else {
            System.out.println("updatedRows = " + updatedRows + "productIds.size()" + productIds.size());
        }
    }

    @Transactional
    public void deleteAllReviewsByMember(Long memberId) {
        List<Long> productIds = reviewMapper.getProductIdsByMember(memberId);
        reviewMapper.deleteReviewByMember(memberId);
        int updatedRows = reviewMapper.updateAvgRateOfProducts(productIds);
        if(updatedRows == productIds.size()) {
            System.out.println("모든 상품 평점 업데이트 완료");
        } else {
            System.out.println("updatedRows = " + updatedRows + "productIds.size()" + productIds.size());
        }
    }
}
