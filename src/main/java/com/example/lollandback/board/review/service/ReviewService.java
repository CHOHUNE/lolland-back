package com.example.lollandback.board.review.service;

import com.example.lollandback.board.product.mapper.ProductMapper;
import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.board.review.dto.ReviewDto;
import com.example.lollandback.board.review.dto.ReviewUpdateDto;
import com.example.lollandback.board.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public List<ReviewDto> getAllReviewsByProduct(Long product_id, Integer page, Integer pageSize) {
        Integer offset = page * pageSize;
        return reviewMapper.getAllReviewsByProduct(product_id, offset,  pageSize);
    }

    public Map<String, Object> getAllReviewsByMember(Long member_id, Integer page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = reviewMapper.countAll(member_id);

        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = ((page - 1) / 10) * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);

        if(prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if(nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;
        map.put("reviewList", reviewMapper.getAllReviewsByMember(from, member_id));
        map.put("pageInfo", pageInfo);

        return map;
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
        List<Long> productIds = reviewMapper.getProductIdsByReview(reviewIds);
        reviewMapper.deleteSelectedReviews(reviewIds);
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

    public Long countTotalReview(Long productId) {
        return reviewMapper.countTotalReview(productId);
    }

    public Map<Integer, Long> getRatingDistribution(Long product_id) {
        List<Integer> rates = reviewMapper.getAllRatesByProduct(product_id);

        return rates.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
