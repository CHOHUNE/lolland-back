package com.example.lollandback.board.like.controller;

import com.example.lollandback.board.like.domain.ProductLike;
import com.example.lollandback.board.like.dto.ProductLikeDto;
import com.example.lollandback.board.like.service.ProductLikeService;
import com.example.lollandback.gameBoard.service.LikeService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productLike")
public class ProductLikeController {


    private final ProductLikeService productLikeService;

    // ---------------------------- 좋아요 눌렀을때 프론트에서 백으로 넘기는 정보 로직 ----------------------------
    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody ProductLike productLike,
                                                    @SessionAttribute(value = "login", required = false) com.example.lollandback.member.domain.Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(productLikeService.update(productLike, login));
    }

    // ---------------------------- 상품마다 좋아요 눌렀을때 해당 정보를 db에 저장시키는 로직 ----------------------------
    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long productId,
                                                   @SessionAttribute(value = "login", required = false) com.example.lollandback.member.domain.Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(productLikeService.get(productId, login));
    }

    // ---------------------------- 상품 좋아요 목록을 프론트로 내보내는 로직 ----------------------------
    @GetMapping("detilas")
    public ResponseEntity<List<ProductLikeDto>> getAllLikes(@SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ProductLikeDto> likes = productLikeService.getAllLikes(login);
        return ResponseEntity.ok(likes);
    }
}
