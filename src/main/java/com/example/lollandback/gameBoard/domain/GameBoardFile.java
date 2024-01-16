package com.example.lollandback.gameBoard.domain;

import lombok.Data;

@Data
public class GameBoardFile {
    private Long id;
    private String file_name;
    private String file_url;
    private Long gameboard_id;

}
