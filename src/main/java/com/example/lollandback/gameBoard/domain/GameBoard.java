package com.example.lollandback.gameBoard.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class GameBoard {

    private String title;
    private String board_content;
    private BigInteger id;
    private LocalDateTime reg_time;
    private String category;
    private BigInteger board_count;


}
