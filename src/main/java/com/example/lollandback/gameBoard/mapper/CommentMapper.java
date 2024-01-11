package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.Comment;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
INSERT INTO gameboardcomment(comment_content, game_board_id,parent_id)
VALUES (#{comment_content},#{game_board_id},#{parent_id})
""")
    int insert(Comment comment);


//    @Select("""
//SELECT * FROM gameboardcomment WHERE game_board_id=#{game_board_id}
//""")
//    List<Comment> selectByBoardId(Integer boardId);

@Select("""
        WITH RECURSIVE CommentHierarchy AS (
            SELECT
              id,
              comment_content,
              reg_time,
              parent_id,
              game_board_id,
              member_id,
              1 AS depth
            FROM
              gameboardcomment
            WHERE
              parent_id IS NULL
            UNION ALL
            SELECT
              c.id,
              c.comment_content,
              c.reg_time,
              c.parent_id,
              c.game_board_id,
              c.member_id,
              ch.depth + 1 AS depth
            FROM
              gameboardcomment c
            JOIN
              CommentHierarchy ch ON c.parent_id = ch.id
        )
        SELECT
          id,
          comment_content,
          reg_time,
          parent_id,
          game_board_id,
          member_id,
          depth
        FROM
          CommentHierarchy
        WHERE
          game_board_id = #{game_board_id}
        ORDER BY
          parent_id,reg_time
        """)
        List<Comment> selectByBoardId(Integer game_board_id);




    @Delete(""" 
DELETE FROM gameboardcomment WHERE id=#{id}
""")
    void deleteByBoardId(Integer id);


    @Update("""
UPDATE gameboardcomment SET comment_content=#{comment_content} WHERE id=#{id}
""")
    boolean update(Comment comment);
}
