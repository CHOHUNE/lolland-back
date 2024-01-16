package com.example.lollandback.board.qna.controller;

import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.QnaDto;
import com.example.lollandback.board.qna.service.QnaService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/list")
    public List<QnaDto> fetchList(@RequestParam Long product_id) {
        System.out.println("product_id = " + product_id);
        return qnaService.getQnaByProduct(product_id);
    }

    @PostMapping("/submit")
    public ResponseEntity addQuestion(@SessionAttribute("login") Member login, @RequestBody Question question) {
        question.setMember_id(login.getId());
        try {
            qnaService.addQuestion(question);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("문의 등록 중 에러 발생: " + e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
