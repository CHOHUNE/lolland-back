package com.example.lollandback.gameBoard.service;

import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.gameBoard.mapper.LikeMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper likeMapper;

    public Map<String,Object> update(Like like,Member login) {

        like.setMember_id(login.getMember_login_id());

        int count = 0;
        if (likeMapper.delete(like) == 0) {
            count = likeMapper.insert(like);
        }
        int countLike = likeMapper.countByBoardId(like.getGame_board_id());

        return Map.of("like", count == 1,
                "countLike", countLike);
    }


    public Map<String, Object> get(Integer boardId, Member login) {
        int countLike = likeMapper.countByBoardId(boardId);
        Like like = null;
        if (login != null) {
            like = likeMapper.selectByBoardId(boardId,login.getMember_login_id());

        }
        return Map.of("like", like != null, "countLike", countLike);
    }

    public List<Like> getList(Member login) {
        return likeMapper.selectLoginId(login.getMember_login_id());
    }
}
