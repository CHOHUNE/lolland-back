package com.example.lollandback.member.controller;

import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.member.domain.EditMemberAndAddress;
import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.domain.MemberAndAddress;
import com.example.lollandback.member.dto.EditPasswordDto;
import com.example.lollandback.member.dto.MemberDto;
import com.example.lollandback.member.dto.SetRandomPasswordDto;
import com.example.lollandback.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService service;

    // 회원 가입
    @PostMapping("signUp")
    public void addUser(@RequestBody @Valid MemberAndAddress memberAndAddress) {
        service.addUser(memberAndAddress);
    }

    // 아이디 찾기
    @GetMapping("findId")
    public ResponseEntity findId(MemberDto memberDto) {
        return service.findIdByNameAndEmail(memberDto.getMember_name(), memberDto.getMember_email());
    }

    // 비밀번호 찾기
    @GetMapping("findPassword")
    public ResponseEntity findPassword(MemberDto memberDto) {
        return service.findUserByIdAndEmail(memberDto.getMember_login_id(), memberDto.getMember_email());
    }

    // 회원 가입시 아이디 중복 체크
    @GetMapping("checkId")
    public ResponseEntity checkUserId(@RequestParam String member_login_id) {
        if(service.checkUserId(member_login_id) != null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    // 회원 로그인
    @PostMapping("login")
    public ResponseEntity login(@RequestBody Member member, WebRequest request){

        if (service.loginUser(member, request)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 회원 로그아웃
    @PostMapping("logout")
    public void logout(HttpSession session) {
        if(session != null) {
            session.invalidate();
        }
    }

    // 로그인 한 맴버로 맴버 ID만 가져오기
    @GetMapping("login")
    public MemberDto getMember(@SessionAttribute("login")Member login) {
        return service.getMember(login);
    }

    // loginProvider에 제공 하는 기능
    @GetMapping("homepageLogin")
    public Member login(@SessionAttribute(value = "login", required = false) Member login){
        return login;
    }

    // 회원정보 조회시 비밀번호 체크
    @GetMapping("checkPassword")
    public ResponseEntity checkPassword(@SessionAttribute("login") Member login, @RequestParam String password) {
        if(service.getLoginIdAndPassword(login.getMember_login_id(), password) != null){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //회원 정보 수정 전 비밀번호 제외 유저의 정보 전달
    @GetMapping("memberInfo")
    public MemberDto getMemberInfo(@SessionAttribute("login")Member login) {
        return service.getMemberInfo(login);
    }

    // 회원 정보 수정
    @PutMapping("edit")
    public ResponseEntity editMember(@RequestBody @Valid EditMemberAndAddress editMemberAndAddress) {
        if (service.editMember(editMemberAndAddress)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 회원 비밀번호 수정
    @PutMapping("editPassword")
    public void editPassword(@SessionAttribute("login")Member login,
                             @RequestBody @Valid EditPasswordDto editPasswordDto) {
        service.editPassword(login, editPasswordDto);

    }

    // 회원 비밀 번호를 임시 비밀 번호로 셋팅 ---------------------------------------------------------
    @PutMapping("setRandomPassword")
    public void setRandomPassword (@RequestBody SetRandomPasswordDto setRandomPasswordDto) {

        service.setRandomPassword(setRandomPasswordDto);
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity delete(@SessionAttribute(value = "login", required = false) Member login) {

        // 비 로그인 유저이면 401 에러 던지기
        if(login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        // 회원 탈퇴 성공시 ok 값 던지기
        if(service.deleteMember(login.getId())) {
            return ResponseEntity.ok().build();
        }

        // 실패시 서버 에러 던지기
        return ResponseEntity.internalServerError().build();
    }

    // 회원 목록 전체 조회 (user인 사람들만)
    @GetMapping("listAll")
    public Map<String, Object> getAllMember(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "id",required = false)String loginId,
                                            @RequestParam(value = "name", required = false)String name) {


        return service.getAllMember(page, loginId, name);
    }

    // 관리자가 탈퇴 회원 id 번호로 삭제하는 로직 (admin만 건드세요)
    @DeleteMapping("DeleteMember/{id}")
    public ResponseEntity deletedMemberByAdmin(@SessionAttribute("login") Member login,
                                    @PathVariable Long id) {
        if (login.getMember_type().equals("admin")) {
            service.deletedMemberByAdmin(id);

            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원이 좋아요 한 게임 게시글 목록 갖고 오기
    @GetMapping("getGameBoardLike")
    public Map<String,Object> getGameBoardLike(@SessionAttribute("login") Member login,
                                               @RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "categoryType", defaultValue = "전체")String categoryType) {
        return service.getGameBoardLike(login, page, categoryType);
    }

    // 회원의 게임 게시글 좋아요 한 것 한개 삭제
    @DeleteMapping("deleteGameBoardLike")
    public ResponseEntity deleteGameBoardLike(@SessionAttribute("login")Member login,
                                    @RequestBody List<Integer> gameBoardId ) {
        if(service.deleteGameBoardLike(login.getMember_login_id(), gameBoardId)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
