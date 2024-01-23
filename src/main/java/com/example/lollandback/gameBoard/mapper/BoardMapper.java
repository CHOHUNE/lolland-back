package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.BoardWriter;
import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.member.domain.Member;
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
    <script>
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
                (<trim prefixOverrides="OR">
                        <if test="category == 'all' or category == 'title'">
                            OR gb.title LIKE #{keyword}
                        </if>
                        <if test="category == 'all' or category == 'content'">
                            OR gb.board_content LIKE #{keyword}
                        </if>
                    </trim>)
                    OR gb.category LIKE #{keyword}
    GROUP BY gb.id

    ORDER BY
            <choose>
                <when test="sortBy == 'board_count'">
                    gb.board_count
                </when>
                <when test="sortBy == 'count_like'">
                    count_like
                </when>
                
                <otherwise>
                    gb.id
                </otherwise>
            </choose> DESC
    LIMIT #{from}, 10
    </script>
""")
    List<GameBoard> selectAll(int from, String keyword, String category,String sortBy);



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
 LIMIT 5
""")
    List<GameBoard> selectTop();

    @Select("""
       
            SELECT DISTINCT gb.*,
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
        SELECT title,id FROM gameboard
 WHERE DATE(reg_time) = CURRENT_DATE
 ORDER BY board_count DESC
 LIMIT 5;
  

""")
    List<GameBoard> selectToday();

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
            <script>
                    
                    SELECT COUNT(*) FROM gameboard
                            WHERE
                     <trim prefixOverrides="OR">
                               <if test="category == 'all' or category == 'title'">
                                   OR title LIKE #{keyword}
                               </if>
                               <if test="category == 'all' or category == 'content'">
                                   OR board_content LIKE #{keyword}
                               </if>
                           </trim>
                       </script>
                    """)
    int countAll(String keyword, String category);


    @Select("""
SELECT * FROM gameboard
WHERE member_id=#{writer}
LIMIT 5
""")
List <GameBoard> selectByMemberId(String writer);

    @Select("""
SELECT m.member_name,m.member_email,m.member_phone_number,m.member_introduce,mi.file_url FROM member m JOIN lolland.memberimage mi ON m.id = mi.member_id
WHERE member_login_id=#{writer} 
            """)
    BoardWriter selectMemberById(String writer);
}
