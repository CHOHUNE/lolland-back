package com.example.lollandback.board.qna.controller;

import com.example.lollandback.board.qna.domain.Answer;
import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.*;
import com.example.lollandback.board.qna.service.QnaService;
import com.example.lollandback.board.review.domain.Review;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/list")
    public Map<String, Object> fetchList(@RequestParam Long product_id,
                                         @RequestParam(value="p", defaultValue = "1") Integer page,
                                         @RequestParam(value = "k", defaultValue = "") String keyword,
                                         @RequestParam(value="c", defaultValue = "all") String category) {
        try {
            return qnaService.getQnaByProduct(product_id, page, keyword, category);
        } catch (Exception e) {
            System.out.println("문의 사항 페이지 가져오는 도중에 에러 발생 = " + e);
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/fetchMine")
    public List<QnaDto> fetchMine(@SessionAttribute("login") Member login, @RequestParam Long product_id) {
        return qnaService.getQnaByMemberAndProduct(login.getId(), product_id);
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

    @DeleteMapping("/delete")
    public ResponseEntity deleteQuestion(@RequestParam Long question_id) {
        try {
            qnaService.deleteQuestionById(question_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("문의 삭제 중 에러 발생 " + e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity editQuestion(@RequestBody QuestionUpdateDto questionUpdateDto) {
        try {
            qnaService.updateQuestion(questionUpdateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("문의 수정 중 에러 발생 " + e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> showQuestion(@SessionAttribute("login") Member login,
                                                              @RequestParam(value="p", defaultValue = "1") Integer page) {
        System.out.println("QnaController.showQuestion");
        Long member_id = login.getId();
        if(member_id != null) {
            try {
                return ResponseEntity.ok(qnaService.viewQuestion(member_id, page));
            } catch (Exception e) {
                System.out.println("문의 불러오는 도중 에러 발생: " + e);
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/admin/{question_id}")
    public ResponseEntity<AnswerReadDto> viewAnswer(@SessionAttribute("login") Member login, @PathVariable Long question_id) {
        if(login.getMember_type().equals("admin")) {
            try{
                return ResponseEntity.ok(qnaService.getQnaDetail(question_id));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/answer/write")
    public ResponseEntity writeAnswer(@SessionAttribute("login") Member login, @RequestBody AnswerWrite answerWrite) {
        if(login.getMember_type().equals("admin")) {
            Long member_id = login.getId();
            try {
               Answer answer = new Answer(member_id, answerWrite);
               qnaService.addAnswer(answer);
               return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/answer/update")
    public ResponseEntity updateAnswer(@SessionAttribute("login") Member login, @RequestBody AnswerUpdate newAnswer) {
        if(login.getMember_type().equals("admin")) {
            try {
                qnaService.updateAnswer(newAnswer);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/answer/delete")
    public ResponseEntity deleteAnswer(@SessionAttribute("login") Member login, @RequestParam Long answer_id) {
        if(login.getMember_type().equals("admin")) {
            try {
                qnaService.deleteAnswerById(answer_id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
