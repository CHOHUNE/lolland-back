package com.example.lollandback.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberAddress {
    private String member_address_name;
    private String member_address;
    private String member_detail_address;
    private String member_post_code;
    private String member_address_type;
    private LocalDateTime reg_time;
}
