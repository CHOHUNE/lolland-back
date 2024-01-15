package com.example.lollandback.board.qna.controller;

import com.example.lollandback.board.qna.dto.QnaDto;
import com.example.lollandback.board.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/list")
    public List<QnaDto> fetchList(@RequestParam Long product_id) {
        return qnaService.getQnaByProduct(product_id);
    }
}
