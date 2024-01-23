package com.example.lollandback.member.service;

import com.example.lollandback.member.dto.EmailSendCodeDto;
import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEmailService {
    private final MemberMapper memberMapper;


    @Autowired
    private JavaMailSender javaMailSender;

    public void emailSendCode(EmailSendCodeDto emailSendCodeDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailSendCodeDto.getMember_email());
        message.setSubject("lolland에서 보내는 회원 가입 인증 코드 입니다.");
        message.setText("회원님의 가입 번호는 : " + emailSendCodeDto.getMessage() + " 입니다.");
        javaMailSender.send(message);
    }

    public void emailFindId(EmailSendCodeDto emailSendCodeDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailSendCodeDto.getMember_email());
        message.setSubject("lolland에 가입 하신 ID 입니다.");
        message.setText("회원님의 ID : " + emailSendCodeDto.getMessage() + " 입니다.");
        javaMailSender.send(message);
    }

    public void sendPasswordMail(EmailSendCodeDto emailSendCodeDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailSendCodeDto.getMember_email());
        message.setSubject("[lolland] 임시 비밀번호가 발급 되었습니다.");
        message.setText("회원님의 비밀 번호는 : " + emailSendCodeDto.getMessage() + " 입니다.");
        javaMailSender.send(message);
    }

    // 회원 가입시 이메일 체크
    public boolean checkUserEmail(String memberEmail) {
        if(memberMapper.checkUserEmail(memberEmail)==1){
            return true;
        } else {
            return false;
        }
    }
}
