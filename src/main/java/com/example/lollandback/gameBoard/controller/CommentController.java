package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.domain.Comment;
import com.example.lollandback.gameBoard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/comment")
@RestController
public class CommentController {
    private final CommentService coomentService;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment){


        if(coomentService.validate(comment)){
            if(coomentService.add(comment)){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.internalServerError().build();
            }
        }else{
            return ResponseEntity.badRequest().build();

        }
    }
    @GetMapping("list")
    public List<Comment> list(@RequestParam("id")Integer boardId){
        return coomentService.list(boardId);
    }

}
