package com.example.lollandback.member.service;

import com.example.lollandback.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private MemberMapper mapper;
}
