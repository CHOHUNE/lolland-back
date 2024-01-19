package com.example.lollandback.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditPasswordDto {

    @NotBlank(message = "비밀번호를 입력 해주세요.")
    @Size(min = 3, max = 25, message = "비밀번호는 최소 3글자 부터 최대 25글자 사이로 작성해주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*~]).*$", message = "비밀번호는 숫자,대문자,소문자,특수기호[!@#$%^&*~]중 하나를 꼭 포함 해야 합니다.")
    private String member_password;
}
