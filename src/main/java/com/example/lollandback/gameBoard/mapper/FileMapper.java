package com.example.lollandback.gameBoard.mapper;

import com.example.lollandback.gameBoard.domain.GameBoardFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("""
            INSERT INTO gameboardfile (gameboard_id,file_name,file_url)
            VALUES (#{gameboard_id},#{file_name},#{file_url})
            """)
    int insert(Long gameboard_id,String file_name,String file_url);

    @Select("""
            SELECT * FROM gameboardfile WHERE gameboard_id=#{gameboard_id}
            """)
    List<GameBoardFile> selectNamesBygameboardId(Integer gameboard_id);


    @Delete("""
            DELETE FROM gameboardfile WHERE gameboard_id=#{boardId} 
            """)
    int deleteByBoard(Integer boardId);

    @Select("""
            SELECT * FROM gameboardfile WHERE id=#{id}
            """)
    GameBoardFile selectById(Integer id);

    @Delete("""
                DELETE FROM gameboardfile
                WHERE id = #{id}
            """)
    int deleteById(Integer id);

}
