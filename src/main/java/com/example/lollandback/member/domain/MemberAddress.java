package com.example.lollandback.member.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberAddress {
    private Long id;


    private String member_address_name;

    @NotBlank(message = "주소를 입력해 주세요.")
    private String member_address;

    private String member_detail_address;

    @NotBlank(message = "우편 번호를 입력해 주세요.")
    private String member_post_code;
    private String member_address_type;
    private LocalDateTime reg_time;
}
