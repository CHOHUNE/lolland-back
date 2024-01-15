package com.example.lollandback.member.controller;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.dto.MemberAddressDto;
import com.example.lollandback.member.service.MemberAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberAddress")
public class MemberAddressController {
    private final MemberAddressService service;

    // 로그인 한 유저의 주소 등록 하기
    @PostMapping
    public void add(@RequestBody @Valid MemberAddress memberAddress,
                    @SessionAttribute("login")Member login ) {
        service.addAddress(memberAddress, login);
        System.out.println("memberAddress = " + memberAddress);
    }

    // 로그인 한 해당 유저의 주소 목록 가져오기
    @GetMapping("loginUser")
    public List<MemberAddressDto> getAddress(@SessionAttribute("login")Member login) {
        return service.getAddressByLoginUser(login);
    }

    // 로그인 한 해당 유저의 해당 주소 (한개) 삭제하기
    @DeleteMapping("deleteAddress/{addressId}")
    public ResponseEntity deleteAddress(@SessionAttribute("login")Member login, @PathVariable Long addressId) {

        if(service.deleteAddressByMemberAndAddressId(login.getId(),addressId)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 로그인 한 유저가 수정 요청한 주소 (한개) 수정하기
    @PutMapping("editAddress")
    public ResponseEntity editAddress(@SessionAttribute("login")Member login, @RequestBody @Valid MemberAddress memberAddress) {
        System.out.println("memberAddress = " + memberAddress);
        return service.editAddressByMemberAndAddressId(login.getId(), memberAddress);
    }


}
