package com.example.lollandback.board.qna.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnaDto {
    private Long question_id;
    private String question_title;
    private String member_login_id;
    private LocalDateTime question_reg_time;
    private String question_content;
    private String answer_content;
    private LocalDateTime answer_reg_time;
}
