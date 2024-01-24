package com.example.lollandback.member.service;

import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.gameBoard.mapper.LikeMapper;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;
    private final MemberAddressMapper memberAddressMapper;
    private final MemberImageMapper memberImageMapper;
    private final LikeMapper gameBoardLikeMapper;

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

        // 기본 이미지 경로
        String fileUrl = urlPrefix + "lolland/user/default/defaultImage.png";
        // 회원 가입시 기본 이미지 설정
        memberImageMapper.insertDefaultImage(member.getId(), fileUrl);
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

    public Map<String, Object> getAllMember(Integer page, String loginId, String name) {
        // 프론트에 리턴할 정보들
        Map<String, Object> map = new HashMap<>();

        // 페이지 정보
        Map<String, Object> pageInfo = new HashMap<>();

        // 마지막 페이지를 정하기 위해 모든 회원 수를 조회 (user만)
        int countUser = mapper.countAllMember(loginId, name);
        //
        int lastPageNumber = (countUser - 1) / 10 + 1;

        // 시작 페이지
        int startPageNumber = (page-1) / 5 * 5 + 1;

        // 마지막 페이지
        int endPageNumber = startPageNumber + 4;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        // 이전 페이지
        int prevPageNumber = startPageNumber - 5;
        // 다음 페이지
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        // 이전 버튼은 0보다 클때만
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        // 다음페이지 버튼은 마지막 페이지보다 작거나 같을때만
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }


        // 프론트로부터 받은 페이지 넘버
        int from = (page - 1) * 10;

        // 모든 회원 정보
        map.put("allMember", mapper.getAllMember(from, loginId, name));
        map.put("pageInfo", pageInfo);

        return map;
    }

    public void deletedMemberByAdmin(Long id) {
        // 회원 탈퇴전 주소 삭제
        memberAddressMapper.deleteByMemberId(id);
        // 회원 탈퇴
        mapper.deleteById(id);
    }


    public List<GameBoard> getGameBoardLike(Member login) {
        return mapper.getGameBoardLikeByLoginId(login.getMember_login_id());
    }

    // 회원의 게임 게시글 좋아요 한 것 한개 삭제
    public boolean deleteGameBoardLike(String memberLoginId, Integer gameBoardId) {
        Like like = new Like();

        like.setMember_id(memberLoginId);
        like.setGame_board_id(gameBoardId);
        if (gameBoardLikeMapper.delete(like)==1) {
            return true;
        } else {
            return false;
        }
    }
}
