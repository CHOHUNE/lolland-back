package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearLike;
import com.example.lollandback.gearBoard.service.GearLikeService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gearlike")
@RequiredArgsConstructor
public class GearLikeController {

    private final GearLikeService gearLikeService;

    @PostMapping // 응답에 정보를 MAP으로 넘길꺼다

    public ResponseEntity like(@RequestBody GearLike gearLike,
                               @SessionAttribute(value = "login",required = false)Member login){

        System.out.println("gearLike = " + gearLike);
        if (login==null){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
         return  ResponseEntity.ok(gearLikeService.update(gearLike,login));
       }



       @GetMapping("/board/{gear_id}")
        public ResponseEntity<Map<String,Object>> get(@PathVariable Integer gear_id,
                                                      @SessionAttribute(value = "login",required = false)Member login){
            return ResponseEntity.ok(gearLikeService.get(gear_id,login));

       }


}
