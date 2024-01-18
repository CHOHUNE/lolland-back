package com.example.lollandback.member.dto;

import lombok.Data;

@Data
public class SetRandomPasswordDto {
    private String member_login_id;
    private String member_password;
}
