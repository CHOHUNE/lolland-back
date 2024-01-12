package com.example.lollandback.gameBoard.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class GameBoard {

    private String title;
    private String board_content;
    private Long id;
    private LocalDateTime reg_time;
    private String category;
    private Long count_comment;
    private Long count_like;
    private Long board_count;



}
