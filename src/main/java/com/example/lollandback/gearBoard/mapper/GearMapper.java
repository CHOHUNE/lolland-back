package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.member.dto.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GearMapper {

    @Insert("""
                insert into gearboard (gear_title, gear_content, category) values (#{gear_title},#{gear_content},#{category});
        """)

    int save(GearBoard gearBoard);

    @Select("""
    select gear_id, gear_title, gear_content, category, gear_inserted, gear_views, gear_recommand, member_name
    from gearboard join lolland.member m on gearboard.member_id = m.id;
    """)
    List<GearBoard> list( );



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
                    select * from member ;
    """)
    Member getm();
}

