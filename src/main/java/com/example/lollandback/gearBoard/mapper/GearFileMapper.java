package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearBoardFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GearFileMapper {
    
    @Insert("""
                insert into gearboardfile (file_name, file_url, gameboard_id) 
                values (#{file_name},#{file_url},#{gameboard_id});
        """)
    int insert(Integer gearId, String originalFilename, String fileUrl);


    @Select("""
                select * from gearboardfile where gearboard_id=#{gearboard_id};
        """)
    List<GearBoardFile> selectNamesBygearboardId(Integer gearboard_id);
}
