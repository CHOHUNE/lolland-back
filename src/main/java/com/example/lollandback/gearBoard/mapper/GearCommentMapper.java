package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GearCommentMapper {

    @Insert("""
            insert into comment (gear_id, comment, memberId) values (#{gear_id},#{comment},#{memberId});
        """)
     int add(GearComment gearComment);
}
