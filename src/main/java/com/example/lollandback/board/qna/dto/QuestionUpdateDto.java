package com.example.lollandback.board.qna.dto;

import lombok.Data;

@Data
public class QuestionUpdateDto {
    private Long question_id;
    private String question_title;
    private String question_content;
}
