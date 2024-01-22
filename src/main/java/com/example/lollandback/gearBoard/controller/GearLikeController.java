package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearLike;
import com.example.lollandback.gearBoard.service.GearLikeService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gearlike")
@RequiredArgsConstructor
public class GearLikeController {

    private final GearLikeService gearLikeService;

    @PostMapping
    public ResponseEntity like(@RequestBody GearLike gearLike,
                               @SessionAttribute(value = "login",required = false)Member login){

        System.out.println("gearLike = " + gearLike);
        if (login==null){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        gearLikeService.update(gearLike,login);
           return null;
       }
}
