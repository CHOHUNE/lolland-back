package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.GameBoard;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {
    @Insert("""
INSERT INTO gameboard(title,board_content,category)
VALUES (#{title},#{board_content},#{category})
""")
    int insert(GameBoard gameBoard);

    @Select("""
            SELECT id, board_content, title, category, board_count, reg_time
            FROM gameboard
            ORDER BY reg_time ASC
            
            """)
    List<GameBoard> selectAll();

    @Select("""
SELECT id, title, board_content, reg_time,board_count
FROM gameboard
WHERE id=#{id}
""")
    GameBoard selectById(Integer bgId);

    @Delete("""
         DELETE FROM gameboard
         WHERE id=#{id}
            """)
    int deleteById(Integer bgId);

    @Update("""
UPDATE gameboard
SET title= #{title},
board_content=#{content}
WHERE id= #{id}
""")
    int update(GameBoard gameBoard);



}
