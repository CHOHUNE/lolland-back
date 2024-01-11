package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearBoard {
    private Integer gear_id;
    private String  gear_title ;
    private String  gear_content;
    private String  gear_writer ;
    private LocalDateTime gear_inserted ;
}
