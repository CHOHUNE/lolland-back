//package com.example.lollandback.gearBoard.controller;
//
//import com.example.lollandback.gearBoard.domain.GearComment;
//import com.example.lollandback.gearBoard.service.GearCommentService;
//import com.example.lollandback.member.domain.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/gearboard")
//public class GearCommentController {
//
//    private final GearCommentService service;
//
//    @PostMapping("addcomment")
//    public ResponseEntity add(@RequestBody GearComment gearComment){
//
//if (service.validate(gearComment)){
//        if(service.add(gearComment)){
//    return  ResponseEntity.ok().build();
//        }else {
//            return ResponseEntity.internalServerError().build();
//        }
//    }else {
//return ResponseEntity.badRequest().build();}
//    }
//}
