package com.example.lollandback.gameBoard.service;


import com.example.lollandback.gameBoard.domain.Comment;
import com.example.lollandback.gameBoard.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;


    public boolean validate(Comment comment) {
        if(comment ==null){
            return false;
        }
        if(comment.getGame_board_id()==null || comment.getGame_board_id() <1){
            return false;
        }
        if(comment.getComment_content() ==null || comment.getComment_content().isBlank()){
            return false;
        }
        return true;
    }

    public boolean add(Comment comment) {

        return commentMapper.insert(comment)==1;
    }

    public List<Comment> list(Integer boardId) {
        return commentMapper.selectByBoardId(boardId);
    }

    @DeleteMapping
    public void remove(Integer boardId){
          commentMapper.deleteByBoardId(boardId) ;
    }

    public boolean update(Comment comment) {
        return commentMapper.update(comment);
    }
}
