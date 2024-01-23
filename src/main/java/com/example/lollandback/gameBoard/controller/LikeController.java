package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.gameBoard.service.LikeService;
import com.example.lollandback.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService service;

    @PostMapping
    public ResponseEntity<Map<String,Object>> like(@RequestBody Like like,@SessionAttribute(value ="login",required = false)Member login) {

        if (login == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(service.update(like,login));
    }

    @GetMapping("gameboard/{boardId}")
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable Integer boardId,
            @SessionAttribute(value="login",required = false)Member login
    ) {
        return ResponseEntity.ok(service.get(boardId,login));
    }

    @GetMapping("gameboard/likedpost")
    public  ResponseEntity <List<Like>> listGet(
            @SessionAttribute(value = "login", required = false) Member login
    ) {
        return ResponseEntity.ok(service.getList(login));
    }

}

