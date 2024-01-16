package com.example.lollandback.gameBoard.controller;


import com.example.lollandback.gameBoard.domain.Comment;
import com.example.lollandback.gameBoard.service.CommentService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/comment")
@RestController
public class
CommentController {
    private final CommentService commentService;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment,@SessionAttribute(value="login",required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (commentService.validate(comment)) {
            if (commentService.add(comment,login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();

        }
    }

    @GetMapping("list/{id}")
    public List<Comment> list(@PathVariable Integer id) {

        return commentService.list(id);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable Integer id) {
        commentService.remove(id);
    }

    @PutMapping("edit")
    public void update(@RequestBody Comment comment) {
        commentService.update(comment);
    }
}
