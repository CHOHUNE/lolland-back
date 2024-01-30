package com.example.lollandback.member.service;

import com.example.lollandback.board.cart.mapper.CartMapper;
import com.example.lollandback.board.like.mapper.ProductLikeMapper;
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
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

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
    private final CartMapper cartMapper;
    private final ProductLikeMapper productLikeMapper;

    private final MemberEmailService memberEmailService;

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

    public ResponseEntity loginUser(Member member, WebRequest request) {
        // 탈퇴 된 회원은 해당 아이디로 로그인 불가능
        if(mapper.findDeletedMember(member.getMember_login_id()) == 1){
            return ResponseEntity.badRequest().body("탈퇴한 회원 입니다.");
        }
        // 아이디가 존재 하면
        Member dbMember = mapper.selectById(member.getMember_login_id());
        if (dbMember != null ) {
            if(dbMember.getMember_password().equals(member.getMember_password())){
                // 비밀번호 숨기기
                dbMember.setMember_password("");
                // 세션에 쿠키를 넣어 클라이언트에 저장 시킴 @SessionAttribute의 login객체에 저장 시킴
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().body("아이디와 비밀번호가 일치하는지 확인해 주세요.");
    }

    // 유저 삭제 로직
    public boolean deleteMember(Member login) {
        try {
            // 탈퇴 유저에게 메일 보내기 ---------------------------------------------------
            memberEmailService.deletedMemberSendMail(login);

            // 삭제 되는 유저의 티입을 deleted 로 변경 ---------------------------------------
            // 이메일, 핸드폰 번호, 탈퇴 유저 처리
            mapper.deleteMemberInfoEditById(login.getId());

            // 삭제 되는 유저의 sub 주소들 삭제 ---------------------------------------------
            memberAddressMapper.deleteSubAddressByMemberId(login.getId());

            // 삭제 되는 유저의 s3 이미지 삭제 ----------------------------------------------
            // 이미지가 변경된다면 일단 기존 파일 이름을 갖고 온다
            String prevFileName = memberImageMapper.getPrevFileName(login.getId());

            // 기존 이미지가 S3의 경로에 존재하면 삭제하기
            String deleteKey = "lolland/user/" + login.getId() + "/" + prevFileName;
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(deleteKey)
                    .build();
            // S3 기존 이미지 파일 삭제
            s3.deleteObject(deleteObjectRequest);

            // 탈퇴 유저 이미지 DB에 삭제된 유저 이미지로 변경 -----------------------------------
            // 탈퇴 이미지 경로 설정
            String fileUrl = urlPrefix + "lolland/user/default/deletedImage.png";
            memberImageMapper.deletedMemberImage(login.getId(),fileUrl);

            // 탈퇴 유저의 게임 게시글 좋아요 삭제 ---------------------------------------------
            gameBoardLikeMapper.deleteByMemberId(login.getMember_login_id());

            // 탈퇴 유저의 장바구니 삭제 -----------------------------------------------------
            cartMapper.deleteAllByMember(login.getId());

            // 탈퇴 유저의 상품 찜 목록 삭제 -------------------------------------------------
            productLikeMapper.deleteLikeByMemberId(login.getId());

            // 모든 작업이 성공시 true 리턴
            return true;
        } catch (Exception e) {
            // 작업중 실패했다면 false 리턴
            e.printStackTrace();
            return false;
        }
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
        // 탈퇴 된 회원은 해당 아이디로 로그인 불가능
        if(mapper.findDeletedMember(memberLoginId) == 1){
            return ResponseEntity.badRequest().body("탈퇴한 회원 입니다.");
        }

        Integer existUser = mapper.findUserByIdAndEmail(memberLoginId, memberEmail);
        if(existUser == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("일치하는 회원 정보가 없습니다.");
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

    // 관리자가 회원 탈퇴 시키는 로직
    public boolean deletedMemberByAdmin(Long id) {
        try {
            // 회원 id번호로 로그인ID 불러오기
            String memberLoginId = mapper.getMemberLoginIdById(id);
            // 회원 id번호로 이메일 불러오기
            String memberEmail = mapper.getMemberEmailById(id);

            // 관리자가 삭제한 유저에게 이메일 보내기
            memberEmailService.deletedByAdminSendMail(memberEmail, memberLoginId);

            // 삭제 되는 유저의 티입을 deleted 로 변경 ---------------------------------------
            // 이메일, 핸드폰 번호, 탈퇴 유저 처리
            mapper.deleteMemberInfoEditById(id);

            // 삭제 되는 유저의 sub 주소들 삭제 ---------------------------------------------
            memberAddressMapper.deleteSubAddressByMemberId(id);

            // 삭제 되는 유저의 s3 이미지 삭제 ----------------------------------------------
            // 이미지가 변경된다면 일단 기존 파일 이름을 갖고 온다
            String prevFileName = memberImageMapper.getPrevFileName(id);

            // 기존 이미지가 S3의 경로에 존재하면 삭제하기
            String deleteKey = "lolland/user/" + id + "/" + prevFileName;
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(deleteKey)
                    .build();
            // S3 기존 이미지 파일 삭제
            s3.deleteObject(deleteObjectRequest);

            // 탈퇴 유저 이미지 DB에 삭제된 유저 이미지로 변경 -----------------------------------
            // 탈퇴 이미지 경로 설정
            String fileUrl = urlPrefix + "lolland/user/default/deletedImage.png";
            memberImageMapper.deletedMemberImage(id, fileUrl);

            // 탈퇴 유저의 게임 게시글 좋아요 삭제 ---------------------------------------------
            gameBoardLikeMapper.deleteByMemberId(memberLoginId);

            // 탈퇴 유저의 장바구니 삭제 -----------------------------------------------------
            cartMapper.deleteAllByMember(id);

            // 탈퇴 유저의 상품 찜 목록 삭제 -------------------------------------------------
            productLikeMapper.deleteLikeByMemberId(id);

            // 모든 작업이 성공시 true 리턴
            return true;
        } catch (Exception e) {
            // 작업중 실패했다면 false 리턴
            e.printStackTrace();
            return false;
        }


    }


    public Map<String, Object> getGameBoardLike(Member login, Integer page, String categoryType) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        // 페이지당 보여줄 컬럼의 시작
        int from = (page -1) * 10;

        // 좋아요 한 게임 게시글 갯수
        int countAll = mapper.countAllGameBoardLikeByLoginId(login.getMember_login_id(), categoryType);

        // 좋아요 한 게시글의 최종 마지막 페이지 번호
        int lastPageNumber = (countAll - 1 ) / 10 + 1;

        // 시작 번호 (버튼 범위에 보여질 시작 번호)
        int startPageNumber = (page - 1) / 5 * 5 + 1;
        // 끝 번호 (버튼 범위에 보여질 마지막)
        int endPageNumber = startPageNumber + 4;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);

        // 이전 버튼
        int prevPageNumber = (startPageNumber - 10);
        // 다음 버튼
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        if(prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if(nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        map.put("gameBoardLikeList",mapper.getGameBoardLikeByLoginId(login.getMember_login_id(), from, categoryType));
        map.put("pageInfo", pageInfo);

        return map;
    }

    // 회원의 게임 게시글 좋아요 한 것 한개 삭제
    public boolean deleteGameBoardLike(String memberLoginId, List<Integer> gameBoardId) {
        Like like = new Like();

        like.setMember_id(memberLoginId);
        if (gameBoardId.size() > 0) {
            for (int i = 0; i < gameBoardId.size(); i++) {
                like.setGame_board_id(gameBoardId.get(i));
                gameBoardLikeMapper.delete(like);
            }
            return true;
        }
        return false;
    }
}
