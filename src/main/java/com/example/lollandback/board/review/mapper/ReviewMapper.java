package com.example.lollandback.board.review.mapper;

import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.board.review.dto.ReviewDto;
import com.example.lollandback.board.review.dto.ReviewUpdateDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    @Select("""
        SELECT r.review_id, r.product_id, r.member_id, r.review_content, r.rate, r.review_reg_time, m.member_login_id
        FROM review r JOIN member m ON r.member_id = m.id
        WHERE r.product_id = #{product_id}
    """)
    List<ReviewDto> getAllReviewsByProduct(Long product_id);

    @Select("""
        SELECT * FROM review
        WHERE member_id = #{member_id}
    """)
    List<ReviewDto> getAllReviewsByMember(Long member_id);

    @Select("""
        SELECT product_id FROM review
        WHERE review_id = #{review_id}
    """)
    Long getProductIdByReview(Long review_id);

    @Select("""
        <script>
            SELECT DISTINCT product_id
            FROM reviews
            WHERE review_id IN
            <foreach collection="reviewIds" item="review_id" open="(" separator="," close=")">
                #{review_id}
            </foreach>
        </script>
    """)
    List<Long> getProductIdsByReview(List<Long> reviewIds);

    @Select("""
        SELECT product_id FROM review
        WHERE member_id = #{memberId}
    """)
    List<Long> getProductIdsByMember(Long memberId);

    @Insert("""
        INSERT INTO review (product_id, member_id, review_content, rate)
        VALUES (#{product_id}, #{member_id}, #{review_content}, #{rate})
    """)
    void addNewReview(Review review);

    @Update("""
        UPDATE products
        SET average_rate = (
            SELECT AVG(CAST(rate AS DOUBLE))
            FROM reviews
            WHERE product_id = #{product_id}
        )
        WHERE product_id = #{product_id};
    """)
    void updateAvgRateOfProduct(Long product_id);

    @Update("""
        UPDATE review
            SET
                review_content = #{review_content},
                rate = #{rate}
            WHERE
                review_id = #{review_id}
    """)
    void updateReview(ReviewUpdateDto updatedReview);

    @Update("""
        <script>
            <foreach collection="productIds" item="productId" separator=";">
                UPDATE products
                SET average_rate = (
                    SELECT AVG(CAST(rate AS DOUBLE))
                    FROM reviews
                    WHERE product_id = #{productId}
                )
                WHERE product_id = #{productId}
            </foreach>
        </script>
    """)
    int updateAvgRateOfProducts(List<Long> productIds);

    @Delete("""
        DELETE FROM review
        WHERE review_id = #{reviewId}
    """)
    void deleteReviewById(Long reviewId);

    //선택된 리뷰 삭제
    @Delete("""
        <script>
            DELETE FROM review
            WHERE review_id IN
            <foreach collection="reviewIds" open="(" seperator="," close=")">
                #{review_id}
            </foreach>
        </script>
    """)
    void deleteSelectedReviews(List<Long> reviewIds);

    //상품 삭제 시 사용, productId로 넘겨주거나 이름 맞춰서 수정하기
    @Delete("""
        DELETE FROM review
        WHERE product_id = #{productId}
    """)
    void deleteReviewByProductId(Long productId);

    //멤버 탈퇴 시 사용
    @Delete("""
        DELETE FROM review
        WHERE member_id = #{memberId}
    """)
    void deleteReviewByMember(Long memberId);

}
