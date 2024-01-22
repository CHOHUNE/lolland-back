package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GearLikeMapper {


    @Delete("""
                delete from lolland.gearlike where gearboardId=#{gearboardId} and memberId=#{memberId};
        """)
    int delete(GearLike gearLike);

    @Insert("""
                insert into lolland.gearlike (gearboardId, memberId) values (#{gearboardId},#{memberId});
        """)
    int insert(GearLike gearLike);
}
