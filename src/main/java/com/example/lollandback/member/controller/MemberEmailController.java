package com.example.lollandback.member.controller;

import com.example.lollandback.member.dto.EmailSendCodeDto;
import com.example.lollandback.member.service.MemberEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberEmail")
public class MemberEmailController {
    private final MemberEmailService service;


    // 회원 가입시 인증번호 발송 로직
    @PostMapping("sendCodeMail")
    public ResponseEntity sendCodeMail(@RequestBody EmailSendCodeDto emailSendCodeDto) {
        // Gmail보내기
        if(service.emailSendCode(emailSendCodeDto)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원 id찾기 메일 보내기
    @PostMapping("findId")
    public void sendIdMail(@RequestBody EmailSendCodeDto emailSendCodeDto) {

        service.emailFindId(emailSendCodeDto);
    }

    // 비밀번호 찾기 메일 보내기
    @PostMapping("findPassword")
    public void sendPasswordMail(@RequestBody EmailSendCodeDto emailSendCodeDto) {

        service.sendPasswordMail(emailSendCodeDto);
    }

    // 회원 가입시 이메일 체크
    // 이미 가입된 이메일이 존재하면 badRequest를 던짐
    @GetMapping("emailCheck")
    public ResponseEntity checkUserEmail(@RequestParam String member_email) {
        if(service.checkUserEmail(member_email)){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

}
