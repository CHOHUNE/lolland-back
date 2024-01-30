package com.example.lollandback.member.service;

import com.example.lollandback.member.dto.EmailSendCodeDto;
import com.example.lollandback.member.mapper.MemberMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEmailService {
    private final MemberMapper memberMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    public boolean emailSendCode(EmailSendCodeDto emailSendCodeDto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailSendCodeDto.getMember_email());
            helper.setSubject("lolland에서 보내는 회원 가입 인증 코드 입니다.");

            String imageUrl = urlPrefix + "lolland/logo/lolland-logo.png";
            String htmlContent =
                    "<html>" +
                        "<body style='text-align: center;'>" +
                            "<div style='width: 350px; height: 250px; border: 5px solid gray; border-radius: 10px; padding: 20px; margin: auto;'>" +
                                "<img src='" + imageUrl + "' alt='로고 이미지' style='width: 100px;'>" +
                                "<h1 style='color: navy; text-align: center;'>회원 가입 인증 코드 </h1>" +
                                "<p style='font-size: 16px; color: black; text-align: center;'>" +
                                    "회원님의 가입 번호는 : " +
                                    "<strong style='color: orange;'>" + emailSendCodeDto.getMessage() + "</strong> " +
                                    "입니다." +
                                "</p>" +
                            "</div>" +
                        "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            return false;
        }
        javaMailSender.send(message);
        return true;
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
