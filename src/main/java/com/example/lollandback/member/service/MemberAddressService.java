package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.dto.MemberAddressDto;
import com.example.lollandback.member.mapper.MemberAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAddressService {
    private final MemberAddressMapper mapper;

    public void addAddress(MemberAddress memberAddress, Member login) {
        // 새로 추가하는 주소가 sub 이면 그냥 추가
        if(memberAddress.getMember_address_type().equals("sub")) {
            mapper.addAddress(memberAddress, login.getId());
        }
        // 새로 추가하는 주소가 main 이면 main 주소 를 sub로 변경 하고 main으로 추가
        if(memberAddress.getMember_address_type().equals("main")) {
            // main 주소 sub로 강등 시키기
            mapper.changeMainAddressType(login.getId());

            // 새로 추가 하는 주소 main 으로 등록 시키기
            mapper.addAddress(memberAddress, login.getId());
        }
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

    public ResponseEntity editAddressByMemberAndAddressId(Long memberId, MemberAddress memberAddress) {
        // 받은 주소 타입이 sub일때만 작동
        if(memberAddress.getMember_address_type().equals("sub")) {
            // 받은 주소가 기존에 main 이었다면
            if (mapper.getMainAddressByMemberIdAndAddressId(memberId,memberAddress.getId()) == 1){
                return ResponseEntity.badRequest().body("기본 주소는 서브 주소로 변경 불가능 합니다.");
            } else {
                // 받은 주소가 기존 sub 라면
                mapper.editAddress(memberAddress);
                return ResponseEntity.ok().build();
            }
        } else {
            // main 주소 sub로 강등 시키기
            mapper.changeMainAddressType(memberId);

            // 새로 추가 하는 주소 main 으로 등록 시키기
            mapper.editAddress(memberAddress);
            return ResponseEntity.ok().build();
        }
    }
}
