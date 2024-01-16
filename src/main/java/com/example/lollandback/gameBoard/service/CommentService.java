package com.example.lollandback.gameBoard.service;


import com.example.lollandback.gameBoard.domain.Comment;
import com.example.lollandback.gameBoard.mapper.CommentMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
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

    public boolean add(Comment comment, Member login) {

        comment.setMember_id(login.getMember_login_id());
        return commentMapper.insert(comment)==1;
    }

    public List<Comment> list(Integer boardId) {
        return commentMapper.selectByBoardId(boardId);
    }

    @DeleteMapping
    public void remove(Integer boardId){
          commentMapper.deleteById(boardId) ;
    }

    public boolean update(Comment comment) {
        return commentMapper.update(comment);
    }
}
