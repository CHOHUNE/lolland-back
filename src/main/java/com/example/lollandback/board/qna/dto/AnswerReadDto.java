package com.example.lollandback.board.qna.dto;

import lombok.Data;

@Data
public class AnswerReadDto {
    private Long question_id;
    private Long answer_id;
    private Long product_id;
    private String product_name;
    private String question_title;
    private String question_content;
    private String answer_content;
}
