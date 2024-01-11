package com.example.lollandback.gameBoard.service;


import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.gameBoard.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameBoardService {

    private final BoardMapper mapper;
    public boolean save(GameBoard gameBoard){
        return mapper.insert(gameBoard)==1;
    }
    public boolean validate(GameBoard gameBoard){
        if(gameBoard == null){
            System.out.println("board null");
            return false;
        }
        if(gameBoard.getBoard_content() == null || gameBoard.getBoard_content().isBlank()){
            System.out.println("content error");
            return false;
        }

        if(gameBoard.getTitle() == null || gameBoard.getTitle().isBlank()){
            System.out.println("title error");
            return false;
        }
        return true;

    }

    public List<GameBoard> list(){
        return mapper.selectAll();
    }

    public GameBoard get(Integer id){
        return mapper.selectById(id);
    }

    public boolean update(GameBoard gameBoard){
      return mapper.update(gameBoard)==1;
    }

    public boolean delete(Integer id) {
        return mapper.deleteById(id) ==1;

    }

    public void boardCount(Integer id) {
        mapper.boardCount(id);

    }
}
