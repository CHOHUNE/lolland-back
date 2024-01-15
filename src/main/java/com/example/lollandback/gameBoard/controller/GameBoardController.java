package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.service.GameBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lollandback.gameBoard.domain.GameBoard;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gameboard")
public class GameBoardController {

    private final GameBoardService gameboardService;

    @PostMapping("/write")
    public ResponseEntity add(GameBoard gameboard, @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files) throws IOException {

        if (!gameboardService.validate(gameboard)) {
            return ResponseEntity.badRequest().body("Invaild request body");
        }

        if (gameboardService.save(gameboard, files)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
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
    public ResponseEntity edit(GameBoard gameBoard,
                               @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
                               @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFiles

    ) throws IOException {


        if (gameboardService.validate(gameBoard)) {
            if (gameboardService.update(gameBoard ,removeFileIds,uploadFiles)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
