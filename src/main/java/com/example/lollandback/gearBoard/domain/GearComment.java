package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearComment {

    private  Integer id;
    private  Integer gear_id;
    private Integer memberId;
    private  String  comment;
    private LocalDateTime inserted;



}
