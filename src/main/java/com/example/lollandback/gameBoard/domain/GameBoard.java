package com.example.lollandback.gameBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameBoard {

    private String title;
    private String content;
    private String writer;
    private Integer bgId;
    private LocalDateTime inserted;


}
