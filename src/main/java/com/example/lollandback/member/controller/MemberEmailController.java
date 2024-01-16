package com.example.lollandback.member.controller;

import com.example.lollandback.member.dto.EmailSendCodeDto;
import com.example.lollandback.member.service.MemberEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberEmail")
public class MemberEmailController {
    private final MemberEmailService service;

    // 회원 가입시 인증번호 발송 로직
    @PostMapping("sendCodeMail")
    public void sendCodeMail(@RequestBody EmailSendCodeDto emailSendCodeDto) {

        // Gmail보내기
        service.emailSendCode(emailSendCodeDto);
    }

    // 회원 id찾기 메일 보내기
    @PostMapping("findId")
    public void sendIdMail(@RequestBody EmailSendCodeDto emailSendCodeDto) {

        service.emailFindId(emailSendCodeDto);
    }

}
