package com.example.lollandback.member.service;

import com.example.lollandback.member.dto.EmailSendCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(EmailSendCodeDto emailSendCodeDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailSendCodeDto.getMember_email());
        message.setSubject("lolland에서 보내는 회원 가입 인증 코드 입니다.");
        message.setText("회원님의 가입 번호는 : " + emailSendCodeDto.getRandomNumber() + " 입니다.");
        javaMailSender.send(message);
    }
}
