package com.example.lollandback.gameBoard.service;

import com.example.lollandback.gameBoard.controller.GameBoardController;
import com.example.lollandback.gameBoard.controller.NotificationController;
import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final GameBoardService gameBoardService;

//    메시지 알림




//    작성자에게 댓글 알림
    public void notifyComment(Integer postId) {
        GameBoard gameBoard = gameBoardService.getOnlyGameBoard(postId);

        String userId = gameBoard.getMember_id(); // 수정된 부분

        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment").data("댓글이 달렸습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    public SseEmitter subscribe(String userId) {

//        현재 클라이언트를 위한 ssEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

//        연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(()->NotificationController.sseEmitters.remove(userId));
        sseEmitter.onTimeout(()->NotificationController.sseEmitters.remove(userId));
        sseEmitter.onError((e)->NotificationController.sseEmitters.remove(userId));

        return sseEmitter;
    }




}
