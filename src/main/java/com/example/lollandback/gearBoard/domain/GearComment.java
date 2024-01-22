package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearComment {

    private  Integer id;
    private  Integer boardid;
    private Long memberId;
    private  String  comment;
    private LocalDateTime inserted;
    private String member_name;



}
