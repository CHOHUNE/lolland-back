package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.service.GameBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lollandback.gameBoard.domain.GameBoard;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gameboard")
public class GameBoardController {

    private final GameBoardService gameboardService;

    @PostMapping("/write")
    public ResponseEntity add(@RequestBody GameBoard gameboard) {

        if (!gameboardService.validate(gameboard)) {
            return ResponseEntity.badRequest().body("Invaild request body");
        }
        if (gameboardService.save(gameboard)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().body("글 작성 실패");
        }

    }


    @GetMapping("list")
    public Map<String, Object> list(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "k", defaultValue = "") String keyword) {


        return gameboardService.list(page, keyword);
    }

    @GetMapping("/id/{id}")
    public GameBoard get(@PathVariable Integer id) {
        gameboardService.boardCount(id);
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
    public ResponseEntity edit(@RequestBody GameBoard gameBoard) {
        if (gameboardService.validate(gameBoard)) {
            if (gameboardService.update(gameBoard)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
