package com.example.lollandback.board.qna.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private Long answer_id;
    private Long question_id;
    private String answer_content;
    private Long member_id;
    private LocalDateTime answer_reg_time;
}
