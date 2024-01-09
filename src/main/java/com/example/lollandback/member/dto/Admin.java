package com.example.lollandback.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admin {
    private String admin_login_id;
    private String admin_password;
    private String admin_department;
    private String admin_status;
    private LocalDateTime reg_time;
}
