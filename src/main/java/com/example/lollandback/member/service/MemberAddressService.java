package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.dto.MemberAddressDto;
import com.example.lollandback.member.mapper.MemberAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAddressService {
    private final MemberAddressMapper mapper;

    public void addAddress(MemberAddress memberAddress, Member login) {
        mapper.addAddress(memberAddress, login.getId());
    }

    public List<MemberAddressDto> getAddressByLoginUser(Member login) {
        return mapper.getAddressByLoginUser(login.getId());
    }

    public boolean deleteAddressByMemberAndAddressId(Long memberId, Long addressId) {
        if(mapper.deleteAddressByMemberAndAddressId(memberId, addressId) == 1){
         return true;
        }
        return false;
    }

}
