package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearBoardFile {
    private Integer id;
    private String name;
    private Integer gearboard_id ;
    private LocalDateTime inserted;

}
