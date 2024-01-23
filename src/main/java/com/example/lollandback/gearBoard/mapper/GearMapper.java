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



//    select distinct gear_id, gear_title, gear_content, category,
//            (select COUNT(DISTINCT f.id) from gearfile f where f.gearboard_id= b.gear_id) countFile
//    from gearboard b join lolland.gearfile f on b.gear_id = f.gearboard_id
//    where category=#{category};



    @Select("""
            SELECT
              b.gear_id,
              b.gear_title,
              b.gear_content,
              b.category,
              b.gear_inserted,
              b.gear_views,
              COUNT(DISTINCT f.id) AS countFile,
              COUNT(DISTINCT c.id) AS commentCount,
              COUNT(DISTINCT l.id) AS countLike
            FROM
              gearboard b
            LEFT JOIN
              lolland.gearfile f ON b.gear_id = f.gearboard_id
            LEFT JOIN
              lolland.gearcomment c ON b.gear_id = c.boardid
            LEFT JOIN
              lolland.gearlike l ON b.gear_id = l.gearboardId
            WHERE
              b.category = #{category}
            GROUP BY
              b.gear_id
    ORDER BY
         b.gear_inserted desc ;
           
            """)
    List<GearBoard> list(String category);



    @Select("""
                select * from gearboard where gear_id=#{gear_id};
        """)
    GearBoard getId(Integer gear_id);

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

    @Select("""    
SELECT
    b.gear_id,
    b.gear_title,
    b.gear_content,
    b.category,
    b.gear_inserted,
    b.gear_views,
    COUNT(DISTINCT f.id) AS countFile,
    COUNT(DISTINCT c.id) AS commentCount,
    COUNT(DISTINCT l.id) AS countLike
FROM
    gearboard b
        LEFT JOIN
    lolland.gearfile f ON b.gear_id = f.gearboard_id
        LEFT JOIN
    lolland.gearcomment c ON b.gear_id = c.boardid
        LEFT JOIN
    lolland.gearlike l ON b.gear_id = l.gearboardId
-- WHERE
--         b.category = #{category}
GROUP BY
    b.gear_id
ORDER BY
    b.gear_inserted desc ;

        """)
    List<GearBoard> listAll();
}

