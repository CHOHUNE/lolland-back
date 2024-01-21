package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearComment;
import com.example.lollandback.member.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GearCommentMapper {

    @Insert("""
            insert into gearcomment (boardid, comment, memberId) values (#{boardid},#{comment},#{memberId});
        """)
     int add(GearComment gearComment);
}
