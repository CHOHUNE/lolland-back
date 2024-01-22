package com.example.lollandback.gameBoard.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardWriter {
    private Long id;
    private String member_login_id;
    private String member_name;
    private String member_phone_number;
    private String member_email;
    private String member_introduce;
    private String file_url;

}
