package com.example.lollandback.gameBoard.service;

import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.gameBoard.mapper.LikeMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LikeService {

    LikeMapper likeMapper;

    public Map<String,Object> update(Like like) {

        int count=likeMapper.countLike(like.getBoardId());

        if (likeMapper.delete(like) == 0) {
            count = likeMapper.insert(like);

        }
        return Map.of("like", count == 1, "countLike", count);
    }

    public Map<String, Object> get(Integer gameboardId) {
        int count = likeMapper.countLike(gameboardId);
        Like like = likeMapper.selectByGameBoardId(gameboardId);

        return Map.of("like", like != null, "countLike", count);
    }


}
