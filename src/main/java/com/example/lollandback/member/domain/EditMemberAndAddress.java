package com.example.lollandback.member.domain;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class EditMemberAndAddress {
    @Valid
    private EditMember member;
    @Valid
    private MemberAddress memberAddress;
}
