package com.example.lollandback.gearBoard.domain;

import com.example.lollandback.gameBoard.domain.GameBoardFile;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GearBoard {
    private Integer gear_id;
    private String  gear_title ;
    private String  gear_content;
    private String category;
    private LocalDateTime gear_inserted ;
    private Integer gear_views;
    private Integer gear_recommand;
    private String gear_uploadFiles;

    private String member_id;

    private List<GearBoardFile> files;



}

