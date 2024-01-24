package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.service.NotificationService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

private final NotificationService notificationService;
public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();



    @GetMapping
    public SseEmitter subscribe(@SessionAttribute(value="login",required = false) Member login){
        if (login == null) {
            return null;
        }

        String userId = login.getMember_login_id();
        SseEmitter sseEmitter = notificationService.subscribe(userId);

        return sseEmitter;
    }
}
