package com.example.lollandback.member.mapper;

import com.example.lollandback.member.dto.MemberImageDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberImageMapper {
    @Insert("""
        INSERT INTO memberimage 
        (
        member_id, 
        file_name, 
        file_url, 
        image_type
        ) VALUES (
        #{id}, 
        'defaultImage.png', 
        'https://lollandproject0108.s3.ap-northeast-2.amazonaws.com/lolland/user/default/defaultImage.png',
        'default'
        )
    """)
    void insertDefaultImage(Long id);


    @Select("""
        SELECT * FROM memberimage
        WHERE member_id = #{id}
    """)
    MemberImageDto selectByMemberIdToImage(Long id);
}
