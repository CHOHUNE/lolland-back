package com.example.lollandback.gameBoard.domain;

import com.example.lollandback.gameBoard.util.AppUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GameBoard {


    private String title;
    private String board_content;
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime reg_time;

    private String category;
    private Long count_comment;
    private Long count_like;
    private Double board_count;
    private Long countFile;
    private String member_id;

    private List<GameBoardFile> files;

    public String getAgo() {
        return reg_time != null ? AppUtil.getAgo(reg_time) : null;
    }


}
