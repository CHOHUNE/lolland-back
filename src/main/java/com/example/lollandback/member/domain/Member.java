package com.example.lollandback.member.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    @NotBlank(message = "회원 아이디가 빈 값입니다.")
    private String member_login_id;
    @NotBlank(message = "비밀번호가 빈 값입니다.")
    private String member_password;
    private String member_name;
    private String member_phone_number;
    private String member_email;
    private String member_type;
}
