package com.example.lollandback.board.qna.mapper;

import com.example.lollandback.board.qna.domain.Answer;
import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.AnswerReadDto;
import com.example.lollandback.board.qna.dto.QnaDto;
import com.example.lollandback.board.qna.dto.QuestionListDto;
import com.example.lollandback.board.qna.dto.QuestionUpdateDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QnaMapper {

    @Select("""
        <script>
            SELECT COUNT(*) 
            FROM question q 
            JOIN member m ON q.member_id = m.id
            JOIN product p ON q.product_id = p.product_id
            WHERE
                 q.product_id = #{productId} AND
                <trim prefixOverrides="OR" prefix="(" suffix=")">
                    <if test="category == 'all' or category == 'title'">
                        OR q.question_title LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'content'">
                        OR q.question_content LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'id'">
                        OR m.member_login_id LIKE #{keyword}
                    </if>
                </trim>
        </script>
    """)
    int countAll(String keyword, String category, Long productId);


    @Select("""
        <script>
            SELECT m.member_login_id, q.question_id, q.question_title, q.question_reg_time, q.question_content, 
            a.answer_content, a.answer_reg_time
            FROM question q 
            JOIN member m ON q.member_id = m.id
            LEFT JOIN answer a ON q.question_id = a.question_id
            LEFT JOIN product p ON q.product_id = p.product_id
            WHERE 
                 q.product_id = #{productId} AND
                <trim prefixOverrides="OR" prefix="(" suffix=")">
                    <if test="category == 'all' or category == 'title'">
                        OR q.question_title LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'content'">
                        OR q.question_content LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'id'">
                        OR m.member_login_id LIKE #{keyword}
                    </if>
                </trim>
                ORDER BY q.question_id DESC
                LIMIT #{from}, 10
        </script>
    """)
    List<QnaDto> getQnaByProduct(Integer from, String keyword, String category, Long productId);

    @Select("""
        SELECT m.member_login_id, q.question_id, q.question_title, q.question_reg_time, q.question_content, 
        a.answer_content, a.answer_reg_time
        FROM question q 
        JOIN member m ON q.member_id = m.id
        LEFT JOIN answer a ON q.question_id = a.question_id
        JOIN product p ON q.product_id = p.product_id
        WHERE p.product_id = #{productId} AND m.id=#{memberId}
    """)
    List<QnaDto> getQnaByMemberAndProductId(Long memberId, Long productId);

    @Insert("""
        INSERT INTO question (member_id, question_content, product_id, question_title)
        VALUES (#{member_id}, #{question_content}, #{product_id}, #{question_title})
    """)
    void addQuestion(Question question);

    @Delete("""
        DELETE FROM answer
        WHERE question_id = #{questionId}
    """)
    void deleteAnswerByQuestionId(Long questionId);

    @Delete("""
        DELETE FROM question
        WHERE question_id = #{questionId}
    """)
    void deleteQuestionById(Long questionId);

    @Update("""
        UPDATE question
            SET
                question_title = #{question_title},
                question_content = #{question_content}
            WHERE
                question_id = #{question_id}
    """)
    void updateQuestionById(QuestionUpdateDto questionUpdateDto);

    @Select("""
        SELECT q.question_id, q.question_title, q.question_reg_time, p.product_name
        FROM question q 
        LEFT JOIN product p ON q.product_id = p.product_id
        WHERE p.member_id = #{memberId}
    """)
    List<QuestionListDto> getQuestionsForAdmin(Long memberId);

    @Select("""
        SELECT 
            q.question_id, 
            q.question_title, 
            q.question_content, 
            a.answer_id,
            a.answer_content,
            p.product_id,
            p.product_name
        FROM question q 
        LEFT JOIN answer a ON q.question_id = a.question_id
        LEFT JOIN product p ON q.product_id = p.product_id
        WHERE q.question_id = #{questionId}
    """)
    AnswerReadDto getQuestionDetail(Long questionId);

    @Insert("""
        INSERT INTO answer (question_id, product_id, answer_content, member_id)
        VALUES (#{question_id}, #{product_id}, #{answer_content}, #{member_id})
    """)
    void addAnswer(Answer answer);
}
