package com.example.lollandback.member.dto;

import lombok.Data;

@Data
public class EmailSendCodeDto {
    private String member_email;
    private Long randomNumber;
}
