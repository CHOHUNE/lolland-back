package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearBoard {
    private Integer gear_id;
    private String  gear_title ;
    private String  gear_content;
    private String category;
    private LocalDateTime gear_inserted ;
    private Integer gear_views;
    private Integer gear_recommand;
}

