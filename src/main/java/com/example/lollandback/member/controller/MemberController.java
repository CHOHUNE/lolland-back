package com.example.lollandback.member.controller;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.dto.MemberDto;
import com.example.lollandback.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService service;

    @PostMapping("signUp")
    public void addUser(@RequestBody @Valid Member member) {
        service.addUser(member);
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

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity delete(@SessionAttribute(value = "login", required = false) Member login) {

        System.out.println("login = " + login);
        if(service.deleteMember(login.getMember_login_id())) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
