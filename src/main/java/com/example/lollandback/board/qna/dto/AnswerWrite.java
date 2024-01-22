package com.example.lollandback.board.qna.dto;

import lombok.Data;

@Data
public class AnswerWrite {
    private Long product_id;
    private Long question_id;
    private String answer_content;
}
