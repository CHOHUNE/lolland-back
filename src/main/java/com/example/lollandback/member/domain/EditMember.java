package com.example.lollandback.member.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditMember {
    private Long id;

    @NotBlank(message = "아이디를 입력 해주세요.")
//    @Size(min = 3, max = 20, message = "아이디는 최소 3글자 최대 5글자 사이로 작성해주세요.")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자로만 입력해주세요.")
    private String member_login_id;

    @NotBlank(message = "이름을 입력 해주세요.")
    private String member_name;

//    @Size(min = 12, max = 13, message = "전화번호는 11자리부터 거나 12자리 이어야 합니다.")
    private String member_phone_number;

//    @Email(message = "유효한 이메일을 입력해 주세요.")
    private String member_email;
    private String member_type;
}
