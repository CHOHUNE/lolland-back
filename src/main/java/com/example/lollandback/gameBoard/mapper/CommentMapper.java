package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.Comment;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
INSERT INTO gameboardcomment(comment_content, parent_id, game_board_id, member_id)
VALUES (#{commentContent},#{partendCommentId},#{boardId},#{memberId})
""")
    int insert(Comment comment);


    @Select("""
SELECT * FROM gameboardcomment WHERE game_board_id=#{boardId}
""")
    List<Comment> selectByBoardId(Integer boardId);

    @Delete("""
DELETE FROM gameboardcomment WHERE game_board_id=#{boardId}
""")
    void deleteByBoardId(Integer boardId);
}
