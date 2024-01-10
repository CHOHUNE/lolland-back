package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;

    public void addUser(Member member) {
        mapper.insertUser(member);
    }
}
