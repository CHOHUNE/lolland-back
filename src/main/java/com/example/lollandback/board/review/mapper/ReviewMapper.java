package com.example.lollandback.board.review.mapper;

import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.board.review.dto.MyReviewList;
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
            ORDER BY r.review_id DESC
            LIMIT #{pageSize} OFFSET #{offset}
    """)
    List<ReviewDto> getAllReviewsByProduct(Long product_id, Integer offset, Integer pageSize);

    @Select("""
        SELECT rate
        FROM review
        WHERE product_id = #{product_id}
    """)
    List<Integer> getAllRatesByProduct(Long product_id);


    @Select("""
        SELECT product_id FROM review
        WHERE review_id = #{review_id}
    """)
    Long getProductIdByReview(Long review_id);

    @Select("""
        <script>
            SELECT DISTINCT product_id
            FROM review
            WHERE review_id IN
            <foreach collection="reviewIds" item="review_id" open="(" separator="," close=")">
                #{review_id}
            </foreach>
        </script>
    """)
    List<Long> getProductIdsByReview(List<Long> reviewIds);

    @Select("""
        SELECT DISTINCT product_id FROM review
        WHERE member_id = #{memberId}
    """)
    List<Long> getProductIdsByMember(Long memberId);

    @Select("""
        SELECT COUNT(*)
        FROM review r
        JOIN member m ON r.member_id = m.id
        WHERE r.member_id = #{member_id}
    """)
    int countAll(Long member_id);

    @Select("""
        SELECT r.review_id, r.review_content, r.rate, r.review_reg_time, p.product_id, p.product_name
        FROM review r
        LEFT JOIN product p ON r.product_id = p.product_id
        INNER JOIN member m ON r.member_id = m.id
        WHERE r.member_id = #{member_id}
        ORDER BY r.review_reg_time DESC
        LIMIT #{from}, 10
    """)
    List<MyReviewList> getAllReviewsByMember(Integer from, Long member_id);

    @Select("""
        SELECT COUNT(*)
        FROM review
        WHERE product_id = #{productId}
    """)
    Long countTotalReview(Long productId);

    @Insert("""
        INSERT INTO review (product_id, member_id, review_content, rate)
        VALUES (#{product_id}, #{member_id}, #{review_content}, #{rate})
    """)
    void addNewReview(Review review);

    @Update("""
        UPDATE product
        SET average_rate = (
            SELECT AVG(CAST(rate AS DOUBLE))
            FROM review
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
                UPDATE product
                SET average_rate = (
                    SELECT AVG(CAST(rate AS DOUBLE))
                    FROM review
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
            <foreach collection="reviewIds" item="reviewId" open="(" separator="," close=")">
              #{reviewId}
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
