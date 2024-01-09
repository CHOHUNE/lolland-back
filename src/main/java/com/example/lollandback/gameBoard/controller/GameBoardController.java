package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.service.GameBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lollandback.gameBoard.domain.GameBoard;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gameBoard")
public class GameBoardController {


    private final GameBoardService gameboardService;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody GameBoard gameboard
                              ) {

        if (!gameboardService.validate(gameboard)) {

            return ResponseEntity.ok().build();
        }
        if (gameboardService.save(gameboard)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("list")
    public List<GameBoard> list() {
        return gameboardService.list();
    }

    @GetMapping("id/{id}")
    public GameBoard get(@PathVariable Integer id) {
        return gameboardService.get(id);
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        if(gameboardService.delete(id)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody GameBoard gameBoard) {
        if(gameboardService.validate(gameBoard)){
            if(gameboardService.update(gameBoard)){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }else{
            return ResponseEntity.badRequest().build();
        }
    }


}
