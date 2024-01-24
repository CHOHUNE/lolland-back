package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.domain.BoardWriter;
import com.example.lollandback.gameBoard.service.GameBoardService;
import com.example.lollandback.gameBoard.service.NotificationService;
import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lollandback.gameBoard.domain.GameBoard;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gameboard")
public class GameBoardController {

    private final GameBoardService gameboardService;
//    private final MemberService memberService;

    @PostMapping("/write")
    public ResponseEntity add(GameBoard gameboard, @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files, @SessionAttribute(value = "login", required = false) Member login) throws IOException {

        if (!gameboardService.validate(gameboard)) {
            return ResponseEntity.badRequest().body("Invaild request body");
        }

        if (gameboardService.save(gameboard, files, login)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("list")
    public Map<String, Object> list(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "k", defaultValue = "") String keyword,
            @RequestParam(value = "c", defaultValue = "all") String category,
            @RequestParam(value="s", defaultValue = "")String sortBy) {

        return gameboardService.list(page, keyword,category,sortBy);
    }

    @GetMapping("list/notice")
    public List<GameBoard> notice() {
        return gameboardService.notice();
    }

    @GetMapping("list/top")
    public List<GameBoard> top() {
        return gameboardService.top();
    }

    @GetMapping("list/today")
    public List<GameBoard> today(){
        return gameboardService.today();
    }

    @GetMapping("list/written/post/{memberId}")
    public List<GameBoard> writtenPost(@PathVariable String memberId) {
        return gameboardService.writtenPost(memberId);
    }

    @GetMapping("list/info/{memberId}")
    public BoardWriter memberInfo(@PathVariable String memberId) {
        return gameboardService.postMemberInfo(memberId);
    }



    @GetMapping("/id/{id}")
    public GameBoard get(@PathVariable Integer id) {

        return gameboardService.get(id);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        if (gameboardService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/edit")
    public ResponseEntity edit(GameBoard gameBoard,
                               @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
                               @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFiles

    ) throws IOException {

        if (gameboardService.validate(gameBoard)) {
            if (gameboardService.update(gameBoard, removeFileIds, uploadFiles)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

//    알람 부분 ->
    private final NotificationService notificationService;
    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/notification/")
    public SseEmitter notification (HttpServletRequest request,@SessionAttribute(value="login",required = false) Member login){
        String userId = login.getMember_login_id();
        SseEmitter sseEmitter = notificationService.subscribe(userId);

        return sseEmitter;
    }

}
