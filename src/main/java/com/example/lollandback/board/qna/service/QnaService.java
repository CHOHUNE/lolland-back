package com.example.lollandback.board.qna.service;

import com.example.lollandback.board.qna.domain.Question;
import com.example.lollandback.board.qna.dto.QnaDto;
import com.example.lollandback.board.qna.dto.QuestionUpdateDto;
import com.example.lollandback.board.qna.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaMapper qnaMapper;


    public List<QnaDto> getQnaByProduct(Long productId) {
        return qnaMapper.getQnaByProduct(productId);
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
