package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    public void addUser(Member member) {
        mapper.insertUser(member);
    }

    public boolean loginUser(Member member, WebRequest request) {
        Member dbMember = mapper.selectById(member.getMember_login_id());

        if (dbMember != null ) {
            if(dbMember.getMember_password().equals(member.getMember_password())){
                // 비밀번호 숨기기
                dbMember.setMember_password("");
                // 세션에 쿠키를 넣어 클라이언트에 저장 시킴 @SessionAttribute의 login객체에 저장 시킴
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMember(String memberLoginId) {
        return mapper.deleteById(memberLoginId) == 1;
    }
}
