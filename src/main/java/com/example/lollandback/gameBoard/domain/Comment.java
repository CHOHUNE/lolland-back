package com.example.lollandback.gameBoard.domain;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer memberId;
    private  Integer boardId;
    private  Integer parentCommentId;
    private  Integer commentId;
    private  LocalDateTime regTime;
    private  String commentContent;



}
