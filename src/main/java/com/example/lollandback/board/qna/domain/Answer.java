package com.example.lollandback.board.qna.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private Long answer_id;
    private Long question_id;
    private String answer_content;
    private Long supplier_id;
    private LocalDateTime answer_regTime;
}
