package com.example.lollandback.board.like.controller;

import com.example.lollandback.board.like.domain.ProductLike;
import com.example.lollandback.board.like.service.ProductLikeService;
import com.example.lollandback.gameBoard.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productLike")
public class ProductLikeController {


    private final ProductLikeService productLikeService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody ProductLike productLike,
                                                    @SessionAttribute(value = "login", required = false) com.example.lollandback.member.domain.Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(productLikeService.update(productLike, login));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long productId,
                                                   @SessionAttribute(value = "login", required = false) com.example.lollandback.member.domain.Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(productLikeService.get(productId, login));
    }
}
