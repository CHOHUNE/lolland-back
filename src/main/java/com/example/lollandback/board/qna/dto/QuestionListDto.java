package com.example.lollandback.board.qna.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionListDto {
    private Long question_id;
    private String product_name;
    private String question_title;
    private LocalDateTime question_reg_time;
}
