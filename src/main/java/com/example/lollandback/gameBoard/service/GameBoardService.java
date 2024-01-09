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
            return false;
        }
        if(gameBoard.getContent() == null || gameBoard.getContent().isBlank()){
            return false;
        }

        if(gameBoard.getTitle() == null || gameBoard.getTitle().isBlank()){
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

}
