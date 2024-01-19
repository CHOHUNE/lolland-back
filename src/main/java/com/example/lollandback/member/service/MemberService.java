package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.EditMemberAndAddress;
import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.domain.MemberAndAddress;
import com.example.lollandback.member.dto.*;
import com.example.lollandback.member.mapper.MemberAddressMapper;
import com.example.lollandback.member.mapper.MemberImageMapper;
import com.example.lollandback.member.mapper.MemberMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;
    private final MemberAddressMapper memberAddressMapper;
    private final MemberImageMapper memberImageMapper;

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
        // 회원 가입시 기본 이미지 설정
        memberImageMapper.insertDefaultImage(member.getId());
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
        // 회원 주소록
        MemberAddressDto memberAddress = memberAddressMapper.selectByMemberIdToMainAddress(login.getMember_login_id());

        // 회원 이미지
        MemberImageDto memberImage = memberImageMapper.selectByMemberIdToImage(login.getId());

        MemberDto member = mapper.selectByMemberId(login.getMember_login_id());
        member.setMemberAddressDto(memberAddress);
        member.setMemberImageDto(memberImage);
        return member;
    }


    public boolean editMember(@Valid EditMemberAndAddress editMemberAndAddress) {
        // 회원 업데이트
        if(mapper.editMember(editMemberAndAddress.getMember())){
            // 메인 주소 업데이트
            if(memberAddressMapper.editMainAddress(editMemberAndAddress.getMember().getId(), editMemberAndAddress.getMemberAddress())){
                return true;
            }
        }
        return false;
    }

    public String checkUserId(String memberLoginId) {
        return mapper.checkUserId(memberLoginId);
    }

    public ResponseEntity findIdByNameAndEmail(String memberName, String memberEmail) {
        String member_login_id = mapper.findIdByNameAndEmail(memberName,memberEmail);
        if ( member_login_id == null) {
            return ResponseEntity.badRequest().body("일치하는 정보가 없습니다.");
        } else {
            return ResponseEntity.ok().body(member_login_id);
        }
    }

    public ResponseEntity findUserByIdAndEmail(String memberLoginId, String memberEmail) {
        Integer existUser = mapper.findUserByIdAndEmail(memberLoginId, memberEmail);
        if(existUser == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    public void setRandomPassword(SetRandomPasswordDto setRandomPasswordDto) {
        mapper.setPasswordByLoginId(setRandomPasswordDto);
    }

    public void editPassword(Member login, EditPasswordDto editPasswordDto) {
        mapper.editPasswordById(login.getId(), editPasswordDto.getMember_password());
    }

    public List<MemberDto> getAllMember() {
        return mapper.getAllMember();
    }

    public void deletedMemberByAdmin(Long id) {
        mapper.deleteById(id);
    }
}
