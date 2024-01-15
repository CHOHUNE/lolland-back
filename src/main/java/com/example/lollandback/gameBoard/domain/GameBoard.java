package com.example.lollandback.gameBoard.domain;

import com.example.lollandback.gameBoard.util.AppUtil;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

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
    private Long countFile;

    private List<GameBoardFile> files;

    public String getAgo() {
        return AppUtil.getAgo(reg_time);
    }

}
