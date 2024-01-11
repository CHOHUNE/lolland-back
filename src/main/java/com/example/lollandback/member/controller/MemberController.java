package com.example.lollandback.member.controller;

import com.example.lollandback.member.domain.Member;
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
@RequestMapping("/api/member/")
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
