package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
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

    // 회원 가입시 인증 번호 메일 ---------------------------------------------------------------------------
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
                            "<div style='width: 350px; height: 200px; border: 5px solid gray; border-radius: 10px; padding: 20px; margin: auto;'>" +
                                "<div style='text-align: center;'>" +
                                    "<img src='" + imageUrl + "' alt='롤랜드 로고 이미지' style='display: block; margin-left: auto; margin-right: auto; width: 100px;'>" +
                                "</div>" +
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

    // 회원 아이디 찾기 메일 ---------------------------------------------------------------------------
    public boolean emailFindId(EmailSendCodeDto emailSendCodeDto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailSendCodeDto.getMember_email());
            helper.setSubject("lolland에 가입 하신 ID 입니다.");

            String imageUrl = urlPrefix + "lolland/logo/lolland-logo.png";
            String htmlContent =
                    "<html>" +
                        "<body style='text-align: center;'>" +
                            "<div style='width: 350px; height: 200px; border: 5px solid gray; border-radius: 10px; padding: 20px; margin: auto;'>" +
                                "<div style='text-align: center;'>" +
                                    "<img src='" + imageUrl + "' alt='롤랜드 로고 이미지' style='display: block; margin-left: auto; margin-right: auto; width: 100px;'>" +
                                "</div>" +
                                "<h1 style='color: navy; text-align: center;'>가입 하신 ID 입니다.</h1>" +
                                "<p style='font-size: 16px; color: black; text-align: center;'>" +
                                    "회원님의 ID : " +
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

    // 임시 비밀번호 발송 ---------------------------------------------------------------------------
    public boolean sendPasswordMail(EmailSendCodeDto emailSendCodeDto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailSendCodeDto.getMember_email());
            helper.setSubject("[lolland] 임시 비밀번호가 발급 되었습니다.");

            String imageUrl = urlPrefix + "lolland/logo/lolland-logo.png";
            String htmlContent =
                    "<html>" +
                        "<body style='text-align: center;'>" +
                            "<div style='width: 350px; height: 200px; border: 5px solid gray; border-radius: 10px; padding: 20px; margin: auto;'>" +
                                "<div style='text-align: center;'>" +
                                    "<img src='" + imageUrl + "' alt='롤랜드 로고 이미지' style='display: block; margin-left: auto; margin-right: auto; width: 100px;'>" +
                                "</div>" +
                                "<h1 style='color: navy; text-align: center;'>가입 하신 ID 입니다.</h1>" +
                                "<p style='font-size: 16px; color: black; text-align: center;'>" +
                                    "회원님의 비밀 번호는 : " +
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

    // 회원 가입시 이메일 체크
    public boolean checkUserEmail(String memberEmail) {
        if(memberMapper.checkUserEmail(memberEmail)==1){
            return true;
        } else {
            return false;
        }
    }

    // 탈퇴 유저에게 메일 발송 ---------------------------------------------------------------------------
    public boolean deletedMemberSendMail(Member login) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(login.getMember_email());
            helper.setSubject("[lolland] 에서 탈퇴 되셨습니다.");

            String imageUrl = urlPrefix + "lolland/logo/lolland-logo.png";
            String htmlContent =
                    "<html>" +
                        "<body style='text-align: center;'>" +
                            "<div style='width: 350px; height: 200px; border: 5px solid gray; border-radius: 10px; padding: 20px; margin: auto;'>" +
                                "<div style='text-align: center;'>" +
                                    "<img src='" + imageUrl + "' alt='롤랜드 로고 이미지' style='display: block; margin-left: auto; margin-right: auto; width: 100px;'>" +
                                "</div>" +
                                "<h1 style='color: navy; text-align: center;'>회원 탈퇴가 완료 되었습니다.</h1>" +
                                "<p style='font-size: 16px; color: black; text-align: center;'>" +
                                    "<strong style='color: orange;'>" + login.getMember_login_id() + "</strong> " +
                                    " 님 의 탈퇴가 완료 되었습니다." +
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

    // 관리자가 탈퇴시킨 유저에게 메일 발송 ---------------------------------------------------------------------------
    public boolean deletedByAdminSendMail(String memberEmail, String memberLoginId) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(memberEmail);
            helper.setSubject("[lolland] 부정 사용 의심으로 탈퇴 되셨습니다.");

            String imageUrl = urlPrefix + "lolland/logo/lolland-logo.png";
            String htmlContent =
                    "<html>" +
                        "<body style='text-align: center;'>" +
                            "<div style='width: 350px; height: 200px; border: 5px solid gray; border-radius: 10px; padding: 20px; margin: auto;'>" +
                                "<div style='text-align: center;'>" +
                                    "<img src='" + imageUrl + "' alt='롤랜드 로고 이미지' style='display: block; margin-left: auto; margin-right: auto; width: 100px;'>" +
                                "</div>" +
                                "<h1 style='color: navy; text-align: center;'>탈퇴 처리 되었습니다.</h1>" +
                                "<p style='font-size: 16px; color: black; text-align: center;'>" +
                                    "<strong style='color: orange;'>" + memberLoginId + "</strong> " +
                                    " 님 의 계정이 부정 사용" +
                                "</p>" +
                                "<p style='font-size: 16px; color: black; text-align: center;'>" +
                                    "의심으로 탈퇴 처리가 되었습니다. " +
                                "</p>" +
                                "<p style='font-size: 16px; color: orange; text-align: center; margin-top: 5px;'>" +
                                    "문의 사항이 있다면 해당 메일로 회신 부탁 드립니다." +
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


}
