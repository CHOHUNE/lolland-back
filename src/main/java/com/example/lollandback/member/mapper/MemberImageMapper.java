package com.example.lollandback.member.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
