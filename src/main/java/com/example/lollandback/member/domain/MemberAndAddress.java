package com.example.lollandback.member.domain;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class MemberAndAddress {
    @Valid
    private Member member;
    @Valid
    private MemberAddress memberAddress;
}
