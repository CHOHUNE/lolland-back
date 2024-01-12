package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.domain.MemberAndAddress;
import com.example.lollandback.member.dto.MemberAddressDto;
import com.example.lollandback.member.dto.MemberDto;
import com.example.lollandback.member.mapper.MemberAddressMapper;
import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;
    private final MemberAddressMapper memberAddressMapper;

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    public void addUser(MemberAndAddress memberAndAddress) {
        Member member = memberAndAddress.getMember();
        MemberAddress memberAddress = memberAndAddress.getMemberAddress();

        // 회원 생성
        mapper.insertUser(member);
        // 주소 생성
        memberAddressMapper.insertAddress(member.getId(),memberAddress);
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

    public boolean deleteMember(Long id) {
        return mapper.deleteById(id) == 1;
    }

    public MemberDto getMember(Member login) {
        return mapper.selectByLoginId(login.getMember_login_id());
    }

    public String getLoginIdAndPassword(String memberLoginId, String password) {
        return mapper.selectByLoginIdAndPassword(memberLoginId, password);
    }

    public MemberDto getMemberInfo(Member login) {
        MemberAddressDto memberAddress = memberAddressMapper.selectByMemberIdToMainAddress(login.getMember_login_id());

        MemberDto member = mapper.selectByMemberId(login.getMember_login_id());
        member.setMemberAddressDto(memberAddress);
        return member;
    }


}
