package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
INSERT INTO gameboardcomment(comment_content, game_board_id,parent_id,member_id)
VALUES (#{comment_content},#{game_board_id},#{parent_id},#{member_id})
""")
    int insert(Comment comment);


//    @Select("""
//SELECT * FROM gameboardcomment WHERE game_board_id=#{game_board_id}
//""")
//    List<Comment> selectByBoardId(Integer boardId);

    @Select("""


            WITH RECURSIVE CommentHierarchy AS (
                           SELECT
                               gc.id,
                               gc.comment_content,
                               gc.reg_time,
                               gc.parent_id,
                               gc.game_board_id,
                               gc.member_id,
                               mi.file_url,
                               CAST(gc.id AS CHAR) AS path,
                               1 AS depth
                           FROM
                               gameboardcomment gc
                           LEFT JOIN
                               member m ON gc.member_id = m.member_login_id
                           LEFT JOIN
                               memberimage mi ON m.id = mi.member_id
                           WHERE
                               gc.parent_id IS NULL
                               AND gc.game_board_id = #{game_board_id}
                               
                           UNION ALL
                          
                           SELECT
                               c.id,
                               c.comment_content,
                               c.reg_time,
                               c.parent_id,
                               c.game_board_id,
                               c.member_id,
                               mi.file_url,
                               CONCAT(ch.path, ',', c.id),
                               ch.depth + 1 AS depth
                           FROM
                               gameboardcomment c
                           JOIN
                               CommentHierarchy ch ON c.parent_id = ch.id
                           LEFT JOIN
                               member m ON c.member_id = m.member_login_id
                           LEFT JOIN
                               memberimage mi ON m.id = mi.member_id
                           WHERE
                               c.game_board_id = #{game_board_id}
                       )
                       
                       SELECT
                           id,
                           comment_content,
                           reg_time,
                           parent_id,
                           game_board_id,
                           member_id,
                           file_url,
                           depth
                       FROM
                           CommentHierarchy
                       
                       ORDER BY
                           path;            
""")
    List<Comment> selectByBoardId(Integer game_board_id);



@Select("""
SELECT id,comment_content,game_board_id from gameboardcomment
WHERE member_id=#{member_id}
ORDER BY id DESC
LIMIT 5

""")
    List<Comment> selectByMemberId(String member_id);


    @Delete(""" 
DELETE FROM gameboardcomment WHERE id=#{id} OR parent_id=#{id}
""")
    void deleteById(Integer id);

    @Delete("""
            DELETE FROM gameboardcomment WHERE game_board_id=#{id}
            """)
    void deleteByBoardId(Integer id);


    @Update("""
UPDATE gameboardcomment SET comment_content=#{comment_content} WHERE id=#{id}
""")
    boolean update(Comment comment);
}
