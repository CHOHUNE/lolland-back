package com.example.lollandback.gameBoard.service;

import com.example.lollandback.gameBoard.controller.GameBoardController;
import com.example.lollandback.gameBoard.domain.GameBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final GameBoardService gameBoardService;

//    메시지 알림
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
        GameBoardController.sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(()->GameBoardController.sseEmitters.remove(userId));
        sseEmitter.onTimeout(()->GameBoardController.sseEmitters.remove(userId));
        sseEmitter.onError((e)->GameBoardController.sseEmitters.remove(userId));

        return sseEmitter;
    }


//    작성자에게 댓글 알림
    public void notifyComment(Integer postId) {
        GameBoard gameBoard = gameBoardService.getOnlyGameBoard(postId);

        String userId = gameBoard.getMember_id(); // 수정된 부분

        if (GameBoardController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = GameBoardController.sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addComment").data("댓글이 달렸습니다."));
            } catch (Exception e) {
                GameBoardController.sseEmitters.remove(userId);
            }
        }
    }




}
