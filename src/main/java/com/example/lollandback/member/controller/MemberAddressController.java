package com.example.lollandback.member.controller;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.dto.MemberAddressDto;
import com.example.lollandback.member.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberAddress")
public class MemberAddressController {
    private final MemberAddressService service;

    // 로그인 한 해당 유저의 주소 목록 가져오기
    @GetMapping("loginUser")
    public List<MemberAddressDto> getAddress(@SessionAttribute("login")Member login) {
        return service.getAddressByLoginUser(login);
    }

    @DeleteMapping("deleteAddress/{addressId}")
    public ResponseEntity deleteAddress(@SessionAttribute("login")Member login, @PathVariable Long addressId) {

        if(service.deleteAddressByMemberAndAddressId(login.getId(),addressId)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }


}
