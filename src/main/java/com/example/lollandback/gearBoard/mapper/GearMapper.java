package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearBoard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GearMapper {
    @Insert("""
                            insert into gearboard (gear_title, gear_content, category,member_id)
                            values (#{gear_title},#{gear_content},#{category},#{member_id});
    """)
    //  생성되기전에 미리  데이터 값 추가하기
    // 파일이 어떤게시물의 파일인지 알아야해서
    @Options(useGeneratedKeys = true,keyProperty = "gear_id")


    int insert(GearBoard gearBoard);






    @Select("""
                    select * from gearboard where category=#{category};
    """)
    List<GearBoard> list(String category);



    @Select("""
                select * from gearboard where gear_id=#{gear_id};
        """)
    GearBoard getId(Integer gearId);

    @Delete("""
                delete from gearboard where gear_id=#{gear_id};
        """)

    int remove(Integer gear_id);

    @Update("""
                update gearboard set  category=#{category} ,
                  gear_content=#{gear_content},
                  gear_title=#{gear_title}  
                  where gear_id=#{gear_id};
        """)
    int saveup(GearBoard gearBoard);


    @Select("""
                select * from gearboard where gear_id=#{gear_id};
        """)

    GearBoard selectById(Integer gear_id);
}

