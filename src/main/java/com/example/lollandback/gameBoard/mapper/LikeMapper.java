package com.example.lollandback.gameBoard.mapper;


import com.example.lollandback.gameBoard.domain.Like;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Delete("""
            DELETE FROM gameboardlike 
            WHERE game_board_id =#{game_board_id}
              """)
    int delete(Like like);

    @Insert("""
            INSERT INTO gameboardlike (game_board_id, member_id)
            VALUES (#{game_board_id},#{member_id})
                  """)
    int insert(Like like);

    @Select("""
            SELECT COUNT(id) FROM gameboardlike WHERE game_board_id=#{game_board_id}
            """)
    int countLike(Integer boardId);

    @Select("""
                SELECT id FROM gameboardlike WHERE game_board_id=#{game_board_id}
            """)
    Like selectByGameBoardId(Integer boardId);
}
