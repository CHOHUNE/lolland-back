package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.GameBoard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
INSERT INTO gameboard(title,board_content,category,member_id)
VALUES (#{title},#{board_content},#{category},#{member_id})
""")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(GameBoard gameBoard);

    @Select("""
    SELECT 
        gb.id,
        gb.board_content,
        gb.title,
        gb.category,
        gb.board_count,
        gb.reg_time,
        gb.member_id,
        COUNT(DISTINCT gl.id) as count_like,
        COUNT(DISTINCT gc.id) as count_comment,
        COUNT(DISTINCT gf.id) as countFile
    FROM gameboard gb
    LEFT JOIN lolland.gameboardlike gl ON gb.id = gl.game_board_id
    LEFT JOIN lolland.gameboardcomment gc ON gb.id = gc.game_board_id
    LEFT JOIN lolland.gameboardfile gf ON gb.id = gf.gameboard_id
    WHERE 
        gb.category != '공지' AND
        (title LIKE #{keyword} OR board_content LIKE #{keyword} OR category LIKE #{keyword})
    GROUP BY gb.id
    ORDER BY gb.id DESC
    LIMIT #{from}, 10
""")
    List<GameBoard> selectAll(int from, String keyword);


    @Select("""
SELECT *,COUNT(DISTINCT gl.id)count_like,
 COUNT(DISTINCT gc.id)count_comment,
                   COUNT(DISTINCT gf.id)countFile
               
 from gameboard gb
 LEFT JOIN lolland.gameboardlike gl on gb.id = gl.game_board_id
 LEFT JOIN lolland.gameboardcomment gc on gb.id = gc.game_board_id
               LEFT JOIN lolland.gameboardfile gf on gb.id = gf.gameboard_id
               WHERE gb.category !='공지'
               
 GROUP BY gb.id,gb.board_count
 ORDER BY count_like DESC,count_comment DESC,gb.board_count DESC
 LIMIT 10
""")
    List<GameBoard> selectTop();

    @Select("""
       
            SELECT gb.*,
                (SELECT COUNT(DISTINCT gl.id) FROM lolland.gameboardlike gl WHERE gl.game_board_id = gb.id) AS count_like,
                (SELECT COUNT(DISTINCT gc.id) FROM lolland.gameboardcomment gc WHERE gc.game_board_id = gb.id) AS count_comment,
                (SELECT COUNT(DISTINCT gf.id) FROM lolland.gameboardfile gf WHERE gf.gameboard_id = gb.id) AS countFile
         FROM gameboard gb
         LEFT JOIN lolland.gameboardlike gl ON gb.id = gl.game_board_id
         LEFT JOIN lolland.gameboardcomment gc ON gb.id = gc.game_board_id
         LEFT JOIN lolland.gameboardfile gf ON gb.id = gf.gameboard_id
         WHERE gb.category = '공지'
         ORDER BY gb.id;
         
                       """)
    List<GameBoard> selectNotice();

    @Select("""
SELECT *,
            COUNT(DISTINCT gc.id)count_comment
            FROM gameboard gb
             LEFT JOIN lolland.gameboardcomment gc on gb.id = gc.game_board_id
WHERE gb.id=#{id}
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
