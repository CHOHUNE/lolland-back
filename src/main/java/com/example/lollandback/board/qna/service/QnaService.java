package com.example.lollandback.board.qna.service;

import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.QnaDto;
import com.example.lollandback.board.qna.dto.QuestionUpdateDto;
import com.example.lollandback.board.qna.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaMapper qnaMapper;


    public Map<String, Object> getQnaByProduct(Long productId, Integer page, String keyword, String category) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = qnaMapper.countAll("%" + keyword + "%", category, productId);

        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = ((page - 1) / 10) * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);

        if(prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if(nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;
        System.out.println("from = " + from);
        map.put("qnaList", qnaMapper.getQnaByProduct(from, "%" + keyword + "%", category, productId));
        map.put("pageInfo", pageInfo);
        System.out.println("pageInfo = " + pageInfo);
        return map;
    }

    public void addQuestion(Question question) {
        qnaMapper.addQuestion(question);
    }

    public List<QnaDto> getQnaByMemberAndProduct(Long memberId, Long productId) {
        return qnaMapper.getQnaByMemberAndProductId(memberId, productId);
    }

    @Transactional
    public void updateQuestion(QuestionUpdateDto questionUpdateDto) {
        qnaMapper.updateQuestionById(questionUpdateDto);
    }

    @Transactional
    public void deleteQuestionById(Long questionId) {
        qnaMapper.deleteAnswerByQuestionId(questionId);
        qnaMapper.deleteQuestionById(questionId);
    }

}
