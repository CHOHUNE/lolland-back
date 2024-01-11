package com.example.lollandback.board.review.mapper;

import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.board.review.dto.ReviewDto;
import com.example.lollandback.board.review.dto.ReviewUpdateDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    @Select("""
        SELECT r.review_id, r.product_id, r.member_id, r.review_content, r.rate, m.member_login_id
        FROM review r NATURAL JOIN member m
        WHERE r.product_id = #{product_id}
    """)
    List<ReviewDto> getAllReviewsByProduct(Long product_id);

    @Select("""
        SELECT * FROM review
        WHERE member_id = #{member_id}
    """)
    List<ReviewDto> getAllReviewsByMember(Long member_id);

    @Insert("""
        INSERT INTO review (product_id, member_id, review_content, rate)
        VALUES (#{review.product_id}, #{review.member_id}, #{review.review_content}, #{review.rate})
    """)
    void addNewReview(Review review);

    @Update("""
        UPDATE review
            SET
                review_content = #{updatedReview.review_content},
                rate = #{updatedReview.rate}
            WHERE
                review_id = #{updatedReview.review_id}
    """)
    void updateReview(ReviewUpdateDto updatedReview);

    @Delete("""
        DELETE FROM review
        WHERE review_id = #{reviewId}
    """)
    void deleteReviewById(Long reviewId);

    //선택된 리뷰 삭제
    @Delete("""
        DELETE FROM review
        WHERE review_id IN
        <foreach collection="reviewIds" open="(" seperator="," close=")">
            #{review_id}
        </foreach>
    """)
    void deleteSelectedReviews(List<Long> reviewIds);

    //멤버 탈퇴 시 사용
    @Delete("""
        DELETE FROM review
        WHERE member_id = #{memberId}
    """)
    void deleteReviewByMember(Long memberId);

}
