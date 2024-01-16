package com.example.lollandback.board.qna.mapper;

import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.QnaDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QnaMapper {

    @Select("""
        SELECT m.member_login_id, q.question_id, q.question_title, q.question_reg_time, q.question_content, 
        a.answer_content, a.answer_reg_time
        FROM question q 
        JOIN member m ON q.member_id = m.id
        JOIN answer a ON q.question_id = a.question_id
        JOIN product p ON q.product_id = p.product_id
        WHERE p.product_id = #{productId}
    """)
    List<QnaDto> getQnaByProduct(Long productId);


    @Insert("""
        INSERT INTO question (member_id, question_content, product_id, question_title)
        VALUES (#{member_id}, #{question_content}, #{product_id}, #{question_title})
    """)
    void addQuestion(Question question);
}
