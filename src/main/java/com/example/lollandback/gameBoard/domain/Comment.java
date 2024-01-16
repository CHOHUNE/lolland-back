package com.example.lollandback.gameBoard.domain;

import com.example.lollandback.gameBoard.util.AppUtil;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class Comment {
    private String member_id;
    private  Integer game_board_id;
    private  Integer parent_id;
    private  Integer id;
    private  LocalDateTime reg_time;
    private  String comment_content;
    private Integer depth;

    public String getAgo(){
        return AppUtil.getAgo(reg_time);
    }



}
