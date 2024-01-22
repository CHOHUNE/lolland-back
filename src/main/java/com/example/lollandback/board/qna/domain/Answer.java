package com.example.lollandback.board.qna.domain;

import com.example.lollandback.board.qna.dto.AnswerWrite;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private Long answer_id;
    private Long product_id;
    private Long question_id;
    private String answer_content;
    private Long member_id;
    private LocalDateTime answer_reg_time;

    public Answer() {

    }

    public Answer(Long member_id, AnswerWrite answerWrite) {
        this.member_id = member_id;
        this.question_id = answerWrite.getQuestion_id();
        this.product_id = answerWrite.getProduct_id();
        this.answer_content = answerWrite.getAnswer_content();
    }
}
