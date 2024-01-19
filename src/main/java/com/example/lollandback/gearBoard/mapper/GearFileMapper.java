package com.example.lollandback.gearBoard.mapper;

//import com.example.lollandback.gearBoard.domain.GearBoardFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GearFileMapper {
//    @Insert("""
//            insert into gearfile (name, gearboard_id) values (#{gearboard_id},#{name});
//            """)
//    int insert(Integer gearId, String originalFilename);

    @Insert("""
            insert into gearfile (gearboard_id,name) values (#{gear_id},#{originalFilename});
            """)
    int insert(Integer gear_id, String originalFilename);

    @Select("""
                select name from gearfile where gearboard_id =#{gearId};
        """)

    List<String> selectNameByGearboardId(Integer gearId);
}
