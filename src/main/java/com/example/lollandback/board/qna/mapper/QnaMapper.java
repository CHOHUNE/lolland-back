package com.example.lollandback.board.qna.mapper;

import com.example.lollandback.board.qna.domain.Answer;
import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.*;
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
                ORDER BY q.question_reg_time DESC
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
                question_content = #{question_content},
                question_reg_time = CURRENT_TIMESTAMP
            WHERE
                question_id = #{question_id}
    """)
    void updateQuestionById(QuestionUpdateDto questionUpdateDto);

    @Select("""
        SELECT q.question_id, q.question_title, q.question_reg_time, p.product_name
        FROM question q 
        LEFT JOIN product p ON q.product_id = p.product_id
        WHERE p.member_id = #{memberId}
        ORDER BY q.question_reg_time DESC
        LIMIT #{from}, 10
    """)
    List<QuestionListDto> getQuestionsForAdmin(Integer from, Long memberId);

    //해당 멤버가 등록한 "상품"의 question
    @Select("""
        SELECT COUNT(*)
        FROM question q
        LEFT JOIN product p ON q.product_id = p.product_id
        WHERE p.member_id = #{memberId}
    """)
    int countAllQuestions(Long memberId);

    //해당 멤버가 작성한 question
    @Select("""
        SELECT COUNT(*)
        FROM question q
        WHERE q.member_id = #{memberId}
    """)
    int countAllMemberQuestion(Long memberId);

    @Select("""
        SELECT q.question_id, q.question_reg_time, q.question_title, a.answer_id, p.product_name
        FROM question q
        LEFT JOIN answer a ON q.question_id = a.question_id
        LEFT JOIN product p ON q.product_id = p.product_id
        WHERE q.member_id = #{member_id}
        ORDER BY q.question_reg_time DESC
        LIMIT #{from}, 10
    """)
    List<MyQuestionDto> getAllQnaByMember(Integer from, Long member_id);

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

    @Update("""
        UPDATE answer
            SET answer_content = #{answer_content},
                answer_reg_time = CURRENT_TIMESTAMP
            WHERE answer_id = #{answer_id}
    """)
    void updateAnswer(AnswerUpdate newAnswer);

    @Delete("""
        DELETE FROM answer
        WHERE answer_id = #{answer_id}
    """)
    void deleteAnswerById(Long answer_id);


    // -------- 해당 관리자가 작성한 모든 문의 답변 삭제 --------
    @Delete("""
        DELETE FROM answer
        WHERE member_id = #{member_id}
    """)
    void deleteAnswerByMember(Long member_id);

    // ------- 해당 상품에 등록된 모든 문의 답변 삭제 ----------

    @Delete("""
        DELETE FROM answer
        WHERE product_id = #{product_id}
    """)
    void deleteAnswerByProduct(Long product_id);


    @Select("""
        <script>
            SELECT answer_id
            FROM answer
            WHERE question_id IN
            <foreach collection="questionIds" item="question_id" open="(" seperator="," close=")">
                #{question_id}
            </foreach>
        </script>
    """)
    List<Long> getAllAnswersByQuestions(List<Long> questionIds);

    @Delete("""
        <script>
            DELETE FROM answer
            WHERE answer_id IN
            <foreach collection="answerIds" item="answerId" open="(" separator="," close=")">
                #{answerId}
            </foreach>
        </script>
    """)
    void deleteSelectedAnswers(List<Long> answerIds);

    @Delete("""
        <script>
            DELETE FROM question
            WHERE question_id IN
            <foreach collection="questionIds" item="question_id" open="(" separator="," close=")">
                #{question_id}
            </foreach>
        </script>
    """)
    void deleteSelectedQuestions(List<Long> questionIds);

    @Select("""
        SELECT a.answer_id
        FROM answer a 
        RIGHT JOIN question q ON a.question_id = q.question_id
        WHERE q.member_id = #{memberId}
    """)
    List<Long> getAllAnswerByMember(Long memberId);

    @Delete("""
        DELETE FROM question
        WHERE member_id = #{memberId}
    """)
    void deleteQuestionsByMember(Long memberId);
}
