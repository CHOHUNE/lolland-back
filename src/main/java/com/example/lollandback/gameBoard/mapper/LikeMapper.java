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
    int deleteByBoardId(Integer id);


    @Select("""
SELECT * FROM gameboardlike WHERE member_id=#{memberLoginId}
""")
    List<Like> selectLoginId(String memberLoginId);
}
