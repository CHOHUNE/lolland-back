package com.example.lollandback.gearBoard.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearBoardFile {
    private Long id;
    private String file_name;
    private String file_url;
    private LocalDateTime inserted;
    private Long gearboard_id;
}
