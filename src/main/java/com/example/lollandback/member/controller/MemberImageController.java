package com.example.lollandback.member.controller;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.dto.MemberImageDto;
import com.example.lollandback.member.service.MemberImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberImage")
public class MemberImageController {
    private final MemberImageService service;

    // 로그인 한 유저가 자기의 프로필 사진 변경
    @PutMapping("editMemberImage")
    public void editMemberImage(@RequestParam("image_type")String imageType,
                                @RequestParam(value = "file", required = false)MultipartFile file,
                                @SessionAttribute("login")Member login
                                ) throws IOException {
        System.out.println("imageType = " + imageType);
        System.out.println("file = " + file);
        service.editMemberImage(login, file, imageType);
    }

}
