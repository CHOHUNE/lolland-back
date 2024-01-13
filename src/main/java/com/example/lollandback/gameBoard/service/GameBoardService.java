package com.example.lollandback.gameBoard.service;


import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.gameBoard.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameBoardService {

    private final BoardMapper mapper;

    public boolean save(GameBoard gameBoard) {
        return mapper.insert(gameBoard) == 1;
    }

    public boolean validate(GameBoard gameBoard) {
        if (gameBoard == null) {
            System.out.println("board null");
            return false;
        }
        if (gameBoard.getBoard_content() == null || gameBoard.getBoard_content().isBlank()) {
            System.out.println("content error");
            return false;
        }

        if (gameBoard.getTitle() == null || gameBoard.getTitle().isBlank()) {
            System.out.println("title error");
            return false;
        }
        return true;

    }

    public Map<String, Object> list(Integer page, String keyword) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = mapper.countAll("%" + keyword + "%");
        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = (page - 1) / 10 * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;
        map.put("gameBoardList", mapper.selectAll(from, "%" + keyword + "%"));
        map.put("pageInfo", pageInfo);
        return map;
    }

    public GameBoard get(Integer id) {
        return mapper.selectById(id);
    }

    public boolean update(GameBoard gameBoard) {
        return mapper.update(gameBoard) == 1;
    }

    public boolean delete(Integer id) {
        return mapper.deleteById(id) == 1;

    }

    public void boardCount(Integer id) {
        mapper.boardCount(id);

    }

}
