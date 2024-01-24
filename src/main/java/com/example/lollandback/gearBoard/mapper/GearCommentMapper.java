package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearComment;
import com.example.lollandback.member.domain.Member;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GearCommentMapper {


    @Insert("""
            insert into gearcomment (boardid, comment, memberId) values (#{boardid},#{comment},#{memberId});
        """)
     int add(GearComment gearComment);

    @Select("""
                select member_name ,comment,inserted, gearcomment.id id
                 from gearcomment join lolland.member m on m.id = gearcomment.memberid where boardid=#{gear_id};
        """)
    List<GearComment> list(Integer gear_id);

    @Delete("""
                delete from gearcomment where id=#{id};
        """)
    int remove(Integer id);

    @Select("""
                select m.id id from gearcomment join lolland.member m on m.id = gearcomment.memberid where boardid=#{gear_id};
        """)
    GearComment selectById(Integer id);


}
