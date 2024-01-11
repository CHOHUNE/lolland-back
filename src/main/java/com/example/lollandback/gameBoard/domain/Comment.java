package com.example.lollandback.gameBoard.domain;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer member_id;
    private  Integer game_board_id;
    private  Integer parent_id;
    private  Integer id;
    private  LocalDateTime reg_time;
    private  String comment_content;
    private Integer depth;



}
