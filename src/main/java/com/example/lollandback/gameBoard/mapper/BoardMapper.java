package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.GameBoard;
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
               SELECT gb.id,
                gb.board_content,
                gb.title,
                 gb.category,
                  gb.board_count,
                   gb.reg_time,
                   COUNT(DISTINCT gl.id)count_like,
                   COUNT(DISTINCT gc.id)count_comment 
               FROM gameboard gb
               LEFT JOIN lolland.gameboardlike gl on gb.id = gl.game_board_id
               LEFT JOIN lolland.gameboardcomment gc on gb.id = gc.game_board_id
                 WHERE 
                     title LIKE #{keyword}
                    OR board_content LIKE #{keyword}
               GROUP BY gb.id
               ORDER BY gb.id DESC
               LIMIT #{from},10
                           
               """)
    List<GameBoard> selectAll(int from, String keyword);

    @Select("""
SELECT id, title, board_content, reg_time,board_count
FROM gameboard
WHERE id=#{id}
""")
    GameBoard selectById(Integer id);

    @Delete("""
         DELETE FROM gameboard
         WHERE id=#{id}
            """)
    int deleteById(Integer bgId);

    @Update("""
UPDATE gameboard
SET title= #{title},board_content=#{board_content},category=#{category}
WHERE id= #{id}
""")
    int update(GameBoard gameBoard);

    @Update("UPDATE gameboard SET board_count = board_count + 0.5 WHERE id = #{id}")
    void boardCount(Integer id);

    @Select("""
            SELECT COUNT(*) FROM gameboard
       WHERE title LIKE #{keyword}
       OR board_content LIKE #{keyword}
            """)
    int countAll(String keyword);

}
