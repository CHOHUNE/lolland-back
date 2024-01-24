package com.example.lollandback.board.qna.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyQuestionDto {
    private Long question_id;
    private Long answer_id;
    private String question_title;
    private LocalDateTime question_reg_time;
    private String product_name;
}
