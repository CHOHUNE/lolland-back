package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearComment;
import com.example.lollandback.gearBoard.service.GearCommentService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gearboard/")
public class GearCommentController {

    private final GearCommentService commentservice;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody GearComment gearComment,
                              @SessionAttribute(value = "login",required = false)Member login){

        System.out.println("gearComment = " + gearComment);

if (commentservice.validate(gearComment)){
        if(commentservice.add(gearComment,login)){
    return  ResponseEntity.ok().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }
    }else {
return ResponseEntity.badRequest().build();}
    }
}
