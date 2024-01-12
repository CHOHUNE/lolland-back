package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.gameBoard.service.LikeService;
import com.example.lollandback.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService service;

    @PostMapping
    public void like(@RequestBody Like like) {
        service.update(like);
    }

    @GetMapping("gameboard/{boardId}")
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable Integer boardId
    ) {
        return ResponseEntity.ok(service.get(boardId));
    }
}

