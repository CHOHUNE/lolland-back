package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.Comment;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
INSERT INTO gameboardcomment(comment_content, game_board_id)
VALUES (#{comment_content},#{game_board_id})
""")
    int insert(Comment comment);


    @Select("""
SELECT * FROM gameboardcomment WHERE game_board_id=#{game_board_id}
""")
    List<Comment> selectByBoardId(Integer boardId);

    @Delete("""
DELETE FROM gameboardcomment WHERE id=#{id}
""")
    void deleteByBoardId(Integer id);


    @Update("""
UPDATE gameboardcomment SET comment_content=#{comment_content} WHERE id=#{id}
""")
    boolean update(Comment comment);
}
