package com.example.lollandback.gameBoard.service;

import com.example.lollandback.gameBoard.domain.Like;
import com.example.lollandback.gameBoard.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper likeMapper;

    public void update(Like like) {

        int count = 0;
        if (likeMapper.delete(like) == 0) {
            count = likeMapper.insert(like);
            System.out.println("delete");
        }

    }


    public Map<String, Object> get(Integer boardId) {
        int countLike = likeMapper.countByBoardId(boardId);
        Like like = null;
        like = likeMapper.selectByBoardId(boardId);
        return Map.of("like", like != null, "countLike", countLike);
    }

}
