package com.example.lollandback.board.review.controller;

import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.board.review.dto.GetReviewDto;
import com.example.lollandback.board.review.dto.ReviewDto;
import com.example.lollandback.board.review.dto.ReviewUpdateDto;
import com.example.lollandback.board.review.service.ReviewService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/fetch")
    public GetReviewDto fetchReviews(@RequestParam Long product_id,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Long totalReview = reviewService.countTotalReview(product_id);
        List<ReviewDto> reviewList = reviewService.getAllReviewsByProduct(product_id, page, pageSize);
        return new GetReviewDto(reviewList, totalReview);
    }

    @GetMapping("/fetchAll")
    public List<ReviewDto> fetchMemberReviews(@SessionAttribute("login") Member login) {
        Long member_id = login.getId();
        return reviewService.getAllReviewsByMember(member_id);
    }

    @PostMapping("/submit")
    public ResponseEntity addNewReview(@SessionAttribute("login") Member login, @RequestBody Review review) {
        try {
            Long member_id = login.getId();
            review.setMember_id(member_id);
            //TODO: 해당 회원이 구매 내역에 있는지 조회하는 메소드
            reviewService.addNewReview(review);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("리뷰 등록 중에 에러 발생" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateReview(@RequestBody ReviewUpdateDto updatedReview) {
        System.out.println("ReviewController.updateReview");
        System.out.println("updatedReview = " + updatedReview.toString());
        try {
            reviewService.updateReview(updatedReview);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("리뷰 업데이트 도중 에러 발생 " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //개별 리뷰 삭제
    @DeleteMapping("/delete")
    public ResponseEntity deleteReview(@RequestParam Long review_id) {
        try {
            reviewService.deleteReviewById(review_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("리뷰 삭제 중 에러 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //선택된 리뷰 삭제
    @DeleteMapping("/delete/selected")
    public ResponseEntity deleteSelected(@RequestBody List<Long> review_ids) {
        try {
            reviewService.deleteSelectedReviews(review_ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("선택한 리뷰 삭제 중 에러 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 멤버의 모든 리뷰 삭제
    @DeleteMapping("/delete/{member_id}")
    public ResponseEntity deleteAll(@PathVariable Long member_id) {
        try {
            reviewService.deleteAllReviewsByMember(member_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("멤버의 리뷰 삭제 중 에러 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
