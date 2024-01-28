package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class  GearBoard {
    private Integer gear_id;
    private String  gear_title ;
    private String  gear_content;
    private String category;
    private LocalDateTime gear_inserted ;
    private Integer gear_views;
    private Integer gear_recommand;
    // gear_uploadFiles 는  카운트 파일임
    private String gear_uploadFiles;
   private Integer commnetcount;
   private Integer countFile;

   private Integer countLike;
        private  String member_name;
        private  String  member_introduce;
    private String member_id;

    private  String file_name;
    private String file_url;

    private  String mainfile;
    private List<GearFile> files;



}

