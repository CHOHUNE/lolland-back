package com.example.lollandback.gameBoard.mapper;


import com.example.lollandback.gameBoard.domain.Like;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikeMapper {

    @Insert("""
INSERT INTO gameboardlike (game_board_id, member_id) 
VALUES (#{game_board_id},#{member_id})
""") int insert(Like like);


    @Delete("""

DELETE FROM gameboardlike 
WHERE game_board_id=#{game_board_id}
AND member_id=#{member_id}
""") int delete(Like like);


//    만들어 놓은 것 ------------------------------
    @Delete("""

DELETE FROM gameboardlike 
WHERE member_id=#{member_id}
""") int deleteByMemberId(String member_id);

//    -------------------------------------------

    @Select("""
SELECT COUNT(id) FROM gameboardlike WHERE game_board_id=#{game_board_id}
""")
    int countByBoardId(Integer game_board_id);

    @Select("""
SELECT * FROM gameboardlike WHERE game_board_id=#{game_board_id} AND member_id=#{member_id}
""")
    Like selectByBoardId(Integer game_board_id,String member_id);

    @Delete("""
DELETE FROM gameboardlike WHERE game_board_id=#{id}
""")
    int deleteByBoardId(Long id);


    @Select("""
SELECT gl.id,gl.game_board_id,gl.member_id,g.category,g.title,g.board_content FROM gameboardlike gl
 LEFT JOIN lolland.gameboard g on g.id = gl.game_board_id
 WHERE member_id=#{memberLoginId}

""")
    List<Like> selectLoginId(String memberLoginId);

//    카테고리, 제목, 내용, 좋아요한 시간,게시물 id
}
