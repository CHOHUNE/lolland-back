package com.example.lollandback.board.qna.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Question {
    private Long question_id;
    private Long member_id;
    private Long product_id;
    private String question_title;
    private String question_content;
    private LocalDateTime question_reg_time;
}
