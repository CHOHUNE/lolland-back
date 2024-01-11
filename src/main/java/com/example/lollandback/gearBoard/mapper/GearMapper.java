package com.example.lollandback.gearBoard.mapper;

import com.example.lollandback.gearBoard.domain.GearBoard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GearMapper {

    @Insert("""
            insert into  () values ();
        """)

    int save(GearBoard gearBoard);
}
